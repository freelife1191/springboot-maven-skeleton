package com.project.api.sample.service;

import com.project.api.sample.packet.ReqFileSample;
import com.project.api.sample.packet.ResFileSample;
import com.project.common.domain.CommonResult;
import com.project.exception.file.FileRequestParamRequiredException;
import com.project.utils.common.InfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.project.common.constant.ResCode.SUCCESS;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Sample 파일 서비스
 * created by YJK on 23/04/2020
 */
@Slf4j
@Service
public class SampleFileService {

    @Value("${properties.file.upload-dir}")
    private String fileUploadPath;

    /**
     * Sample 파일 업로드 기본 서비스
     * @param file
     * @param request
     * @param webRequest
     * @return
     * @throws IOException
     */
    public ResponseEntity<ResFileSample> upload(MultipartFile file, HttpServletRequest request, WebRequest webRequest) throws IOException {
        if(file.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(getUploadData(ReqFileSample.builder().file(file).build()));
    }

    /**
     * Sample 파일 ModelAttribute MIX 업로드 서비스
     * @param reqFileSample
     * @param request
     * @param webRequest
     * @return
     * @throws IOException
     */
    public ResponseEntity<ResFileSample> mixUpload(ReqFileSample reqFileSample, HttpServletRequest request, WebRequest webRequest) throws IOException {

        if(reqFileSample.getFile().isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(getUploadData(reqFileSample));
    }

    /**
     * Sample 파일 ModelAttribute CommonResult 리턴 MIX 업로드 서비스
     * @param reqFileSample
     * @param request
     * @param webRequest
     * @return
     * @throws IOException
     */
    public CommonResult<?> mixMapUpload(ReqFileSample reqFileSample, HttpServletRequest request, WebRequest webRequest) throws Exception {

        if(reqFileSample.getFile().isEmpty())
            throw new FileRequestParamRequiredException("첨부된 파일이 없습니다");

        Map<String, Object> map = InfoUtils.setInfoMap(request, webRequest); // 에러 맵 기본 셋팅
        map.put("reqFileSample", getUploadData(reqFileSample));

        CommonResult commonResult = new CommonResult<>(SUCCESS, SUCCESS.getMessage(), map);
        log.info("## commonResult = {}",commonResult);
        return commonResult;
    }


    /**
     * Sample 파일 JSON 업로드 CommonResult 리턴 서비스
     * @param reqFileSample
     * @param request
     * @param webRequest
     * @return
     * @throws Exception
     */
    public CommonResult<?> mixJsonMultiMapUpload(ReqFileSample reqFileSample, HttpServletRequest request, WebRequest webRequest) throws Exception {
        if(reqFileSample.getFiles().length < 1)
            throw new FileRequestParamRequiredException("첨부된 파일이 없습니다");
        return mixJsonMapUpload(reqFileSample, request, webRequest);
    }

    /**
     * Sample 파일 JSON 업로드 CommonResult 리턴 서비스
     * @param reqFileSample
     * @param request
     * @param webRequest
     * @return
     * @throws Exception
     */
    public CommonResult<?> mixJsonOneMapUpload(ReqFileSample reqFileSample, HttpServletRequest request, WebRequest webRequest) throws Exception {
        if(reqFileSample.getFile().isEmpty())
            throw new FileRequestParamRequiredException("첨부된 파일이 없습니다");

        return mixJsonMapUpload(reqFileSample, request, webRequest);
    }

    /**
     * Sample 파일 JSON 업로드 CommonResult 리턴 서비스
     * @param reqFileSample
     * @param request
     * @param webRequest
     * @return
     * @throws Exception
     */
    public CommonResult<?> mixJsonMapUpload(ReqFileSample reqFileSample, HttpServletRequest request, WebRequest webRequest) throws Exception {

        Map<String, Object> map = InfoUtils.setInfoMap(request, webRequest); // 에러 맵 기본 셋팅
        if (reqFileSample.getFile() != null)
            map.put("reqFileSample", getUploadData(reqFileSample));
        if (reqFileSample.getFiles() != null)
            map.put("reqFileSampleList", getMultiUploadList(reqFileSample));

        CommonResult<?> commonResult = new CommonResult<>(SUCCESS, SUCCESS.getMessage(), map);
        log.info("## commonResult = {}",commonResult);
        return commonResult;
    }

    /**
     * Sample 파일 다중 업로드 기본 서비스
     * @param files
     * @param request
     * @param webRequest
     * @return
     * @throws IOException
     */
    public ResponseEntity<List<ResFileSample>> multiUpload(MultipartFile[] files, HttpServletRequest request, WebRequest webRequest) throws IOException {
        return ResponseEntity.ok(getMultiUploadList(ReqFileSample.builder().files(files).build()));
    }

    /**
     * Sample 파일 ModelAttribute CommonResult 리턴 다중 업로드 서비스
     * @param reqFileSample
     * @param request
     * @param webRequest
     * @return
     * @throws IOException
     */
    public CommonResult<?> mixMapMultiUpload(ReqFileSample reqFileSample, HttpServletRequest request, WebRequest webRequest) throws Exception {

        if(reqFileSample.getFile().isEmpty())
            throw new FileRequestParamRequiredException("첨부된 파일이 없습니다");

        Map<String, Object> map = InfoUtils.setInfoMap(request, webRequest); // 에러 맵 기본 셋팅
        map.put("reqFileSampleList", getMultiUploadList(reqFileSample));

        CommonResult<?> commonResult = new CommonResult<>(SUCCESS, SUCCESS.getMessage(), map);
        log.info("## commonResult = {}",commonResult);
        return commonResult;
    }

    /**
     * Sample 파일 다중 업로드 응답 리스트 생성
     * @param reqFileSample
     * @return
     * @throws IOException
     */
    public List<ResFileSample> getMultiUploadList(ReqFileSample reqFileSample) throws IOException {
        if(reqFileSample.getFiles().length == 0)
            return new ArrayList<>();

        List<ResFileSample> resFileSampleList = new ArrayList<>();
        for(MultipartFile file : reqFileSample.getFiles()) {
            reqFileSample.setFile(file);
            ResFileSample resFileSample = getUploadData(reqFileSample);
            if(Objects.nonNull(resFileSample)) resFileSampleList.add(resFileSample);
        }
        return resFileSampleList;
    }

    /**
     * Sample 파일 응답 데이터 생성
     * @param reqFileSample
     * @return
     * @throws IOException
     */
    public ResFileSample getUploadData(ReqFileSample reqFileSample) throws IOException {
        if(reqFileSample.getFile().isEmpty())
            return null;

        String fileName = reqFileSample.getFile().getOriginalFilename();
        Path targetPath = makeUploadPath(fileName);
        // https://thebook.io/006985/ch09/02/03/
        // 파일을 업로드 경로에 복사 한다 기존 파일이 있으면 덮어씌운다
        Files.copy(reqFileSample.getFile().getInputStream(), targetPath, REPLACE_EXISTING);

        return ResFileSample.builder()
                .name(reqFileSample.getName())
                .phoneNumber(reqFileSample.getPhoneNumber())
                .fileName(fileName)
                .fileDownloadUri(targetPath.toRealPath(LinkOption.NOFOLLOW_LINKS).toString())
                .fileType(reqFileSample.getFile().getContentType())
                .size(reqFileSample.getFile().getSize())
                .build();
    }

    /**
     * Sample 파일 다운로드 서비스
     * @param fileName
     * @return
     * @throws IOException
     */
    public ResponseEntity<Resource> download(String fileName) throws IOException {

        Path filePath = getFilePath(fileName);

        // Resource resource = new UrlResource(downPath.toUri());
        Resource resource = new InputStreamResource(Files.newInputStream(filePath));

        if (!resource.exists()) return ResponseEntity.noContent().build();

        //log.debug("resource :: :: {}", resource.getURL().getPath());
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .headers(getHeaders(filePath, fileName))
                .body(resource);
    }

    /**
     * 파일 PATH 가져오기
     * @param fileName
     * @return
     */
    private Path getFilePath(String fileName) {
        // https://blog.naver.com/horajjan/220484659082
        // String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        // Path downPath = Paths.get(fileUploadPath, date, fileName).normalize();
        Path filePath = Paths.get(fileUploadPath, fileName).normalize();
        log.info("## getFilePath : {} ", filePath);
        return filePath;
    }

    /**
     * 업로드 경로 생성
     * @param fileName
     * @return
     * @throws IOException
     */
    private Path makeUploadPath(String fileName) throws IOException {

        Path filePath = getFilePath(fileName);

        // https://thebook.io/006985/ch09/02/02/
        // 파일경로가 존재하지 않을 때만 디렉토리를 생성한다
        if(Files.notExists(filePath))
            Files.createDirectories(filePath);
        return filePath;

    }

    /**
     * HttpHeader 가져오기
     * @param filePath
     * @param fileName
     * @return
     * @throws IOException
     */
    private HttpHeaders getHeaders(Path filePath, String fileName) throws IOException {
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath));
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return headers;
    }


}
