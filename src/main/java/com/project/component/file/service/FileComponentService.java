package com.project.component.file.service;

import com.project.component.file.domain.S3FileInfo;
import com.project.component.file.domain.UploadFileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by KMS on 26/08/2019.
 */
@Service
public class FileComponentService {

    private final S3FileService s3FileService;

    public FileComponentService(S3FileService s3FileService) {
        this.s3FileService = s3FileService;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * TEMP 파일 변경 업로드
     * 파일 업로드 시 serviceName/category/type/contractId/UUID 형식으로 Path를 생성해서 전달
     * @param resource
     * @return
     */
    public UploadFileResponse upload(Resource resource, Path path) {
        return s3FileService.upload(resource, path);
    }

    /**
     * TEMP 파일 변경 다중 업로드
     * 파일 업로드 시 serviceName/category/type/contractId/UUID 형식으로 Path를 생성해서 전달
     * @param resources
     * @return
     */
    public List<UploadFileResponse> upload(List<Resource> resources, Path path) {
        return s3FileService.upload(resources, path);
    }

    /**
     * 파일 업로드
     * 파일 업로드 시 serviceName/category/type/contactId/UUID 형식으로 Path를 생성해서 전달
     * @param multipartFile
     * @return
     */
    public UploadFileResponse upload(MultipartFile multipartFile, Path path) {
        return s3FileService.upload(multipartFile, path);
    }

    /**
     * 다중 파일 업로드
     * @param multipartFiles
     * @return
     */
    public List<UploadFileResponse> upload(MultipartFile[] multipartFiles, Path path) {
        return s3FileService.upload(multipartFiles, path);
    }

    /**
     * S3 파일 이동
     * @param path
     * @param movePath
     */
    public void move(Path path, Path movePath) {
        s3FileService.move(path, movePath);
    }

    /**
     * 파일 다운로드 S3에서 파일 다운로드
     * @param path
     * @return
     */
    public Resource download(Path path) {
        return s3FileService.download(path, bucket);
    }

    /**
     * 바이트형식 파일 다운로드
     * @param path
     * @param fileName
     * @return
     * @throws IOException
     */
    public ResponseEntity<byte[]> byteDownload(String path, String fileName) throws IOException {
        return s3FileService.byteDownload(path, fileName);
    }

    /**
     * 단일 파일 삭제
     * @param path
     * @return
     */
    public List<S3FileInfo> delete(Path path) {
        s3FileService.delete(path);
        return getFileList(path);
    }

    /**
     * 다중 파일 삭제
     * @param fileList
     * @return
     */
    public List<S3FileInfo> delete(List<String> fileList, Path path) {
        s3FileService.delete(fileList, path);
        return getFileList(path);
    }

    /**
     * 다중 파일 삭제
     * @param fileList
     * @return
     */
    public List<Path> delete(List<Path> fileList) {
        s3FileService.delete(fileList);
        return getFileList(fileList);
    }


    /**
     * 특정 경로의 파일 리스트 조회
     * @param fileList
     * @return
     */
    public List<Path> getFileList(List<Path> fileList) {
        return fileList;
    }

    /**
     * 특정 경로의 파일 리스트 조회
     * @param prefix
     * @return
     */
    public List<S3FileInfo> getFileList(Path prefix) {
        return s3FileService.getFileList("/",prefix);
    }

}
