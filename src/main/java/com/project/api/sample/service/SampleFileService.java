package com.project.api.sample.service;

import com.project.api.sample.packet.ResFileSample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Sample 파일 서비스
 * created by KMS on 03/05/2020
 */
@Slf4j
@Service
public class SampleFileService {

    @Value("${properties.file.upload-dir}")
    private String fileUploadPath;

    /**
     * Sample 파일 업로드 서비스
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public ResponseEntity<ResFileSample> upload(MultipartFile multipartFile) throws IOException {

        if(multipartFile.isEmpty())
            return ResponseEntity.noContent().build();

        String fileName = multipartFile.getOriginalFilename();
        Path targetPath = makeUploadPath(fileName);
        // https://thebook.io/006985/ch09/02/03/
        // 파일을 업로드 경로에 복사 한다 기존 파일이 있으면 덮어씌운다
        Files.copy(multipartFile.getInputStream(), targetPath, REPLACE_EXISTING);

        return ResponseEntity.ok(
                new ResFileSample(
                    fileName,
                    targetPath.toRealPath(LinkOption.NOFOLLOW_LINKS).toString(),
                    multipartFile.getContentType(),
                    multipartFile.getSize()
                )
        );

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
        if(resource.exists()) {
            //log.debug("resource :: :: {}", resource.getURL().getPath());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .cacheControl(CacheControl.noCache())
                    .headers(getHeaders(filePath, fileName))
                    .body(resource);
        }
        return ResponseEntity.noContent().build();
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
