package com.project.component.file.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.project.component.file.domain.S3FileInfo;
import com.project.component.file.domain.UploadFileResponse;
import com.project.exception.file.*;
import com.project.utils.common.PathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by KMS on 2019-08-21.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class S3FileService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 버킷 목록 조회
     * @return
     */
    public List<Bucket> getBucketList() {
        List<Bucket> buckets = new ArrayList<>();
        try {
            buckets = amazonS3Client.listBuckets();
            log.info("Bucket List: ");
            for (Bucket bucket : buckets) {
                log.info("    name=" + bucket.getName() + ", creation_date=" + bucket.getCreationDate() + ", owner=" + bucket.getOwner().getId());
            }
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        return buckets;
    }

    /**
     * 컨트롤러에서 호출 MultipartFile -> File 전환
     * @param multipartFile 업로드할 파일
     * @param path S3에 생성될 디렉토리명
     * @return
     * @throws IOException
     */
    public UploadFileResponse upload(MultipartFile multipartFile, Path path) {
        // File s3UploadFile = convert(multipartFile)
        //         .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환 실패"));
        String s3UploadFileUrl = s3Upload(multipartFile, path);

        return new UploadFileResponse(
                multipartFile.getName(),
                s3UploadFileUrl,
                multipartFile.getContentType(),
                multipartFile.getSize()
        );
    }

    /**
     * 컨트롤러에서 호출 MultipartFile -> File 전환 다중 처리
     * @param multipartFiles
     * @param path
     * @return
     */
    public List<UploadFileResponse> upload(MultipartFile[] multipartFiles, Path path) {
        List<UploadFileResponse> resultList = new ArrayList<>();
        for(MultipartFile multipartFile : multipartFiles) {
            UploadFileResponse uploadFileResponse = upload(multipartFile, path);
            resultList.add(uploadFileResponse);
        }
        return resultList;
    }

    /**
     * TEMP 파일 변경 업로드 처리
     * @param resource
     * @param path
     * @return
     */
    public UploadFileResponse upload(Resource resource, Path path) {
        log.info("[S3] Upload File = {}, path = {}",resource.getFilename(), PathUtils.getPath(path));
        UploadFileResponse uploadFileResponse = new UploadFileResponse();
        uploadFileResponse.setFileName(resource.getFilename());
        uploadFileResponse.setFileDownloadUri(putS3(resource, path));
        //업로드 후 파일 S3 업로드 상세경로 리턴
        return uploadFileResponse;
    }


    /**
     * TEMP 파일 변경 다중 업로드 처리
     * @param resources
     * @param path
     * @return
     */
    public List<UploadFileResponse> upload(List<Resource> resources, Path path) {
        List<UploadFileResponse> resultList = new ArrayList<>();
        for(Resource resource : resources) resultList.add(upload(resource, path));
        return resultList;
    }

    /**
     * S3 업로드 정보 셋팅 및 업로드
     * @param s3UploadFile 업로드할 파일
     * @param path S3에 생성될 디렉토리명
     * @return
     */
    private String s3Upload(MultipartFile s3UploadFile, Path path) {
        //S3에 저장될 경로와 파일명 지정
        // String s3UploadfileName = path + "/" + s3UploadFile.getName();
        log.info("[S3] Upload File = {}, path = {}",s3UploadFile.getName(), PathUtils.getPath(path));
        //업로드 후 파일 S3 업로드 상세경로 리턴
        String s3UploadFileUrl = putS3(s3UploadFile, path);
        // removeNewFile(s3UploadFile);

        return s3UploadFileUrl;
    }

    /**
     * S3에 파일 최종 업로드 처리
     * @param resource 업로드할 파일
     * @param path S3에 저장될 상세 경로(경로 + 파일명)
     * @return
     */
    private String putS3(Resource resource , Path path) {
        Map<String, Object> errorMap = new HashMap<>();

        try {

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(resource.contentLength());

            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, PathUtils.getPath(path), resource.getInputStream(), metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException ase) {
            //errorMap.put("AmazonServiceException","Caught an AmazonServiceException from PUT requests, rejected reasons:");
            errorMap.put("Fail FileName:",resource.getFilename());
            errorMap.put("Fail Path:", PathUtils.getPath(path));
            errorMap = getErrorMap(errorMap, ase);
            log.error("[S3] Upload Fail [putS3] :: AmazonServiceException :: {}",errorMap);
            if(ase.getStatusCode() == HttpStatus.NOT_FOUND.value() && ase.getErrorCode().equals("NoSuchKey"))
                throw new FileNotExistException(ase.getErrorCode());
            else if(!( ase.getStatusCode() == HttpStatus.NOT_FOUND.value() && ase.getErrorCode().equals("NoSuchKey"))) // 해당 경로가 존재 하지 않거나
                throw new FileUploadFailException(ase.getErrorCode());
            throw new FileAwsS3ProcessException("[S3] Upload Fail [putS3] :: AmazonServiceException :: "+errorMap);
        } catch (AmazonClientException ace) {
            errorMap.put("Fail FileName:",resource.getFilename());
            errorMap.put("Fail Path:", PathUtils.getPath(path));
            errorMap = getErrorMap(errorMap, ace);
            log.error("[S3] Upload Fail [putS3] :: AmazonClientException :: {}",errorMap);
            throw new FileAwsS3ProcessException("[S3] Upload Fail [putS3] :: AmazonClientException :: "+errorMap);
        } catch (IOException e) {
            throw new FileAwsS3ProcessException(e.getMessage(), e);
        }
        return amazonS3Client.getUrl(bucket, PathUtils.getPath(path)).toString();
    }

    /**
     * S3에 파일 최종 업로드 처리( 멀티파트 처리 )
     * @param s3UploadFile 업로드할 파일
     * @param path S3에 저장될 상세 경로(경로 + 파일명)
     * @return
     */
    private String putS3(MultipartFile s3UploadFile, Path path) {
        Map<String, Object> errorMap = new HashMap<>();

        try {
            /*
             * Obtain the Content length of the Input stream for S3 header
             */
            InputStream is = s3UploadFile.getInputStream();

            Long contentLength = Long.valueOf(IOUtils.toByteArray(is).length);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(contentLength);

            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, PathUtils.getPath(path), s3UploadFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException ase) {
            //errorMap.put("AmazonServiceException","Caught an AmazonServiceException from PUT requests, rejected reasons:");
            errorMap.put("Fail FileName:",s3UploadFile.getName());
            errorMap.put("Fail Path:", PathUtils.getPath(path));
            errorMap = getErrorMap(errorMap, ase);
            log.error("[S3] Upload Fail [putS3] :: AmazonServiceException :: {}",errorMap);
            if(ase.getStatusCode() == HttpStatus.NOT_FOUND.value() && ase.getErrorCode().equals("NoSuchKey"))
                throw new FileNotExistException(ase.getErrorCode());
            else if(!( ase.getStatusCode() == HttpStatus.NOT_FOUND.value() && ase.getErrorCode().equals("NoSuchKey"))) // 해당 경로가 존재 하지 않거나
                throw new FileUploadFailException(ase.getErrorCode());
            throw new FileAwsS3ProcessException("[S3] Upload Fail [putS3] :: AmazonServiceException :: "+errorMap);
        } catch (AmazonClientException ace) {
            errorMap.put("Fail FileName:",s3UploadFile.getName());
            errorMap.put("Fail Path:", PathUtils.getPath(path));
            errorMap = getErrorMap(errorMap, ace);
            log.error("[S3] Upload Fail [putS3] :: AmazonClientException :: {}",errorMap);
            throw new FileAwsS3ProcessException("[S3] Upload Fail [putS3] :: AmazonClientException :: "+errorMap);
        } catch (IOException e) {
            throw new FileAwsS3ProcessException(e.getMessage(), e);
        }
        return amazonS3Client.getUrl(bucket, PathUtils.getPath(path)).toString();
    }

    /**
     * S3 파일 오브젝트 이동 처리
     * @param path
     * @param movePath
     */
    public void move(Path path, Path movePath) {
        try {
            amazonS3Client.copyObject(new CopyObjectRequest(bucket, path.toString(), bucket, movePath.toString()));
        } catch (AmazonClientException e) {
            log.error("[S3] Move Fail [putS3] :: AmazonServiceException :: {}",e.getMessage());
            throw new FileAwsS3ProcessException("savePath = "+path+", movePath = "+movePath);
        }
    }


    /**
     * 파일다운로드
     * 파일 다운로드 예외처리시 유의 사항
     * FileDownloadFailException 임의 예외 발생시 Transactional 에 의해 롤백되지 않도록 noRollbackFor 처리 필요
     * @param path
     * @return
     */
    public Resource download(Path path, String bucket){
        Map<String, Object> errorMap = new HashMap<>();

        Resource resource = null;
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, PathUtils.getPath(path));
            S3Object s3Object = amazonS3Client.getObject(getObjectRequest);
            resource = new InputStreamResource(s3Object.getObjectContent());
            /*
            S3Object s3object = amazonS3Client.getObject(bucket, PathUtils.getPath(path));
            log.info("[S3] Download File Content-Type: " + s3object.getObjectMetadata().getContentType());
            log.info("[S3] Download File Path = {}",PathUtils.getPath(path));
            long contentLength = IOUtils.toByteArray(s3object.getObjectContent()).length;
            resource = new FileInputStreamResource(s3object.getObjectContent(), 0);
            */
            // .txt 파일이면 파일 읽는 부분
            //displayText(s3object.getObjectContent());
            log.info("================== Downloade File Complete ! ==================");
        } catch (AmazonServiceException ase) {
            //log.error("Caught an AmazonServiceException from GET requests, rejected reasons:");
            errorMap.put("Fail Path:", PathUtils.getPath(path));
            errorMap = getErrorMap(errorMap, ase);
            log.warn("[S3] Download Fail [download] :: AmazonServiceException :: {}",errorMap);
            if(ase.getStatusCode() == HttpStatus.NOT_FOUND.value() && ase.getErrorCode().equals("NoSuchKey"))
                throw new FileNotExistException(ase.getErrorCode());

            log.error("[S3] Download Fail [download] :: AmazonServiceException :: {}",errorMap);
            throw new FileDownloadFailException(ase.getErrorCode());
        } catch (AmazonClientException ace) {
            //log.error("Caught an AmazonClientException: ");
            errorMap.put("Fail Path:", PathUtils.getPath(path));
            errorMap = getErrorMap(errorMap, ace);
            log.error("[S3] Download Fail [download] :: AmazonServiceException :: {}",errorMap);
            throw new FileDownloadFailException(ace.getMessage());
        }
        return resource;
    }

    /**
     * 바이트 형식으로 다운로드 받기
     * @param path
     * @param fileName
     * @return
     * @throws IOException
     */
    public ResponseEntity<byte[]> byteDownload(String path, String fileName) throws IOException {
        // String s3fullpath= path+"/"+fileName;
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, path);
        S3Object s3Object = amazonS3Client.getObject(getObjectRequest);
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

        byte[] bytes = IOUtils.toByteArray(objectInputStream);
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    /**
     * 파일 삭제
     * 파일이 존재 하지 않는 경우에는 에러 메세지만 출력 그외 에러 발생시에는 에러발생시킴
     * @param path
     */
    public void delete(Path path) {
        Map<String, Object> errorMap = new HashMap<>();
        try {
            amazonS3Client.deleteObject(bucket, PathUtils.getPath(path));
            log.info("[S3] File Deleted : {}", PathUtils.getPath(path));
        } catch (AmazonServiceException ase) {
            //log.error("Caught an AmazonServiceException from GET requests, rejected reasons:");
            errorMap.put("Fail Path:", PathUtils.getPath(path));
            errorMap = getErrorMap(errorMap, ase);
            log.warn("[S3] File Deleted Fail [delete] :: AmazonServiceException :: {}",errorMap);
            // 삭제 권한이 없으면 AWS S3 권한에러 예외 처리
            if (ase.getStatusCode() == HttpStatus.FORBIDDEN.value() && ase.getErrorCode().equals("AccessDenied"))
                throw new FileAwsS3AccessDeniedException(ase.getErrorCode());
            else if(!( ase.getStatusCode() == HttpStatus.NOT_FOUND.value() && ase.getErrorCode().equals("NoSuchKey"))) // 해당 경로가 존재 하지 않거나
                throw new FileDeleteFailException(ase.getErrorCode());
            log.error("[S3] File Deleted Fail [delete] :: AmazonServiceException :: {}",errorMap);
        } catch (AmazonClientException ace) {
            //log.error("Caught an AmazonClientException: ");
            errorMap.put("Fail Path:", PathUtils.getPath(path));
            errorMap = getErrorMap(errorMap, ace);
            log.error("[S3] File Deleted Fail [delete] :: AmazonServiceException :: {}",errorMap);
            throw new FileDeleteFailException(ace.getMessage());
        }
    }

    /**
     * 다중 파일 삭제
     * @param fileList
     */
    public void delete(List<String> fileList, Path path) {
        fileList.forEach(fileName -> delete(Paths.get(String.valueOf(path), fileName)));
    }

    /**
     * 다중 파일 삭제
     * @param fileList
     */
    public void delete(List<Path> fileList) {
        fileList.forEach(this::delete);
    }

    /**
     * S3에서 조회된 리스트 다중 파일 삭제
     * @param result
     */
    public void delete(ListObjectsV2Result result) {
        result.getObjectSummaries().forEach(os -> delete(Paths.get(os.getKey())));
    }

    /**
     * S3 파일 업로드 하면서 진행마다
     * @param uploadFile
     * @param fileName
     * @return
     */
    private String putS3EventListener(File uploadFile, String fileName) {
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(amazonS3Client).build();
        PutObjectRequest request = new PutObjectRequest(bucket, fileName, uploadFile);
        Upload upload = transferManager.upload(request);

        /* 업로드 progress가 변할 때마다 발생하는 Event Listener */
        request.setGeneralProgressListener(new com.amazonaws.event.ProgressListener() {
            @Override
            public void progressChanged(ProgressEvent progressEvent) {
                double progress = progressEvent.getBytes() != 0 ?
                        (double)progressEvent.getBytesTransferred() / (double)progressEvent.getBytes()*(double)100 : 0.0;
                log.info("progress = "+progress);
            }
        });
        /* 아래 내용을 활용하면, 파일 업로드가 성공할 때까지 block을 걸 수 있다. */
        try {

            upload.waitForCompletion();
        } catch (AmazonClientException amazonClientException) {

            amazonClientException.printStackTrace();
        } catch (InterruptedException e) {

            e.printStackTrace();

        }

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    /**
     * 특정 버켓에 들어있는 데이터 정보 가져오기
     * prefix 를 지정하지 않으면 모든 파일이 출력됨 최대 1000개 제한
     * @return
     */
    public ListObjectsV2Result getObjectsListFromFolder(String delimiter, Path prefix) {
        return amazonS3Client.listObjectsV2(bucket, PathUtils.getPath(prefix));
    }

    /**
     * 특정 버켓에 들어있는 파일 리스트 가져오기
     * prefix 를 지정하지 않으면 모든 파일이 출력됨 최대 1000개 제한
     * @return
     */
    public List<S3FileInfo> getFileList(String delimiter, Path prefix) {
        List<S3ObjectSummary> objects = getObjectsListFromFolder(delimiter, prefix).getObjectSummaries();

        List<S3FileInfo> list = objects.stream()
                .map(o -> new S3FileInfo(
                        o.getBucketName(),
                        o.getKey(),
                        o.getSize(),
                        o.getLastModified().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        o.getStorageClass()
                ))
                .collect(Collectors.toList());
        return list;
    }

    /**
     * 오브젝트 리스트 가져오기
     * @param maxKey
     */
    public void getObjectsList(Integer maxKey) {

        // list all in the bucket
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                    .withBucketName(bucket)
                    .withMaxKeys(maxKey)
                    ;

            ObjectListing objectListing = amazonS3Client.listObjects(listObjectsRequest);

            log.info("Object List:");
            while (true) {
                for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    LocalDateTime localDateTime = objectSummary.getLastModified().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    String time = localDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
                    time = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    log.info("    name=" + objectSummary.getKey() + ", size=" + objectSummary.getSize() + ", owner=" + objectSummary.getOwner().getId());
                    log.info(" bucketName = "+objectSummary.getBucketName()+", etag = "+objectSummary.getETag()+", storageClass = "+objectSummary.getStorageClass()+", LastModified = "+time);

                }

                if (objectListing.isTruncated()) {
                    objectListing = amazonS3Client.listNextBatchOfObjects(objectListing);
                } else {
                    break;
                }
            }
        } catch (AmazonS3Exception e) {
            log.error(e.getErrorMessage());
            System.exit(1);
        }
    }

    /**
     * 폴더 리스트 가져오기
     * @param maxKey
     */
    public void getFolderList(Integer maxKey) {
        // top level folders and files in the bucket
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                    .withBucketName(bucket)
                    .withDelimiter("/")
                    .withMaxKeys(maxKey);

            ObjectListing objectListing = amazonS3Client.listObjects(listObjectsRequest);
            log.info("ObjectListing = "+objectListing);
            log.info("Folder List:");
            for (String commonPrefixes : objectListing.getCommonPrefixes()) {
                log.info("    name=" + commonPrefixes);
            }
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * S3 파일 리스트 가져오기
     * @param prefix
     * @param maxKey
     */
    public void getS3FileList(String prefix, Integer maxKey) {
        // top level folders and files in the bucket
        try {
            ListObjectsRequest fileListRequest = new ListObjectsRequest()
                    .withBucketName(bucket)
//                    .withDelimiter("/")
                    .withPrefix(prefix)
                    .withMaxKeys(maxKey);
            ObjectListing objectFileListing = amazonS3Client.listObjects(fileListRequest);
            log.info("getEncodingType = "+objectFileListing.getEncodingType());
            log.info("getDelimiter = "+objectFileListing.getDelimiter());
            log.info("getMarker = "+objectFileListing.getMarker());
            log.info("getPrefix = "+objectFileListing.getPrefix());
            log.info("File List:");
            for (S3ObjectSummary objectSummary : objectFileListing.getObjectSummaries()) {
                log.info(objectSummary.toString());
//                System.out.println("    name=" + objectSummary.getKey() + ", size=" + objectSummary.getSize() + ", owner=" + objectSummary.getOwner().getId());
            }

            log.info("getCommonPrefixes = "+objectFileListing.getCommonPrefixes());
            for (String commonPrefixes : objectFileListing.getCommonPrefixes()) {
                log.info("commonPrefixes=" + commonPrefixes);
            }

        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
    }

    public void getVersionList() {

        try {
            ListVersionsRequest request = new ListVersionsRequest()
                    .withBucketName(bucket)
                    .withMaxResults(2);
            VersionListing versionListing = amazonS3Client.listVersions(request);
            int numVersions = 0, numPages = 0;
            while (true) {
                numPages++;
                for (S3VersionSummary objectSummary :
                        versionListing.getVersionSummaries()) {
                            log.info("Retrieved object %s, version %s\n",
                            objectSummary.getKey(),
                            objectSummary.getVersionId());
                    numVersions++;
                }
                // Check whether there are more pages of versions to retrieve. If
                // there are, retrieve them. Otherwise, exit the loop.
                if (versionListing.isTruncated()) {
                    versionListing = amazonS3Client.listNextBatchOfVersions(versionListing);
                } else {
                    break;
                }
            }
            log.info(numVersions + " object versions retrieved in " + numPages + " pages");
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }

    // .txt 파일 읽어드리는 함수
    private static void displayText(InputStream input) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null)
                break;
            log.info(line);
        }
    }

    // 파일 URL 가져오기
    public String getFileURL(String fileName) {
        String imageName = (fileName).replace(File.separatorChar, '/');
        String imageUrl = amazonS3Client
                .generatePresignedUrl(new GeneratePresignedUrlRequest(bucket, imageName)).toString();
        return imageUrl;
    }

    /**
     * MultipartFile -> File 변환시 생성된 파일 제거
     * @param targetFile
     */
    private void removeNewFile(File targetFile) {
        log.info("S3 removeNewFile Path = {}, Name = {}",targetFile.getPath(),targetFile.getName());

        if (targetFile.delete()) {
            log.info("{} 파일이 삭제되었습니다.",targetFile.getName());
        } else {
            log.info("삭제할 {} 파일이 없습니다.",targetFile.getName());
        }
    }

    /**
     * MultipartFile -> File 로 변환
     * @param file
     * @return
     */
    private Optional<File> convert(MultipartFile file){

        log.debug("S3 convert fileName = {}",file.getOriginalFilename());
        // String tempDir = System.getProperty(property);
        File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        log.debug("## FILE PATH = {}",convertFile.getPath());

        try {
            file.transferTo(convertFile);
            return Optional.of(convertFile);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * AWS Exception 에러 메세지 셋팅
     * @param errorMap
     * @param error
     * @return
     */
    private Map<String, Object> getErrorMap(Map<String, Object> errorMap, Object error) {
        if(error instanceof AmazonServiceException) {
            AmazonServiceException ase = (AmazonServiceException) error;
            errorMap.put("Error Message", ase.getMessage());
            errorMap.put("HTTP Status Code", ase.getStatusCode());
            errorMap.put("AWS Error Code", ase.getErrorCode());
            errorMap.put("Error Type", ase.getErrorType());
            errorMap.put("Request ID", ase.getRequestId());
        } else if(error instanceof AmazonClientException) {
            AmazonClientException ace = (AmazonClientException) error;
            errorMap.put("Error Message",ace.getMessage());
        }
        return errorMap;
    }

}
