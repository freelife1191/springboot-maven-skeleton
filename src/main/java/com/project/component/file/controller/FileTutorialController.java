package com.project.component.file.controller;

import com.project.component.file.domain.S3FileInfo;
import com.project.component.file.domain.UploadFileResponse;
import com.project.component.file.service.FileComponentService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by KMS on 2019-08-19.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/tutorial/file")
@Api(value = "File", tags = "파일 컨트롤러")
@ApiIgnore
public class FileTutorialController {

    private final FileComponentService fileService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 단일 파일 업로드
     * @param file
     * @return
     */
    @ApiOperation(value = "단일 파일 업로드",notes = "한개의 파일 업로드시 사용", httpMethod = "POST", produces = "multipart/form-data")
    @ApiResponses({@ApiResponse(code = 200, message = "업로드 성공시 S3 파일 업로드 파일정보 리턴")})
    @PostMapping("/upload")
    public ResponseEntity<UploadFileResponse> upload(
            @ApiParam(value = "파일") @RequestPart(value = "file", required = true) MultipartFile file) throws IOException {

        if(file == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(fileService.upload(file, Paths.get("static")));
    }

    /**
     * 다중 파일 업로드
     * @param files
     * @return
     */
    @ApiOperation(value = "다중 파일 업로드", notes = "다수의 파일을 업로드할때 사용 다중 파일 업로드는 Swagger에서 안됨 POSTMAN에서 진행", httpMethod = "POST", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses({@ApiResponse(code = 200, message = "업로드 성공시 S3 파일 업로드 파일정보 리스트 리턴")})
    @PostMapping(value = "/uploadList", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<UploadFileResponse>> upload(
            @ApiParam(value = "파일 리스트") @RequestPart(value = "files",required = true) MultipartFile[] files) {
        /*
        log.info("======= HEADER =======");
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String name = (String) headers.nextElement();
            String value = request.getHeader(name);
            log.info(name + "=" + value);
        }

        log.info("======= Body =======");
        DataInputStream dis = new DataInputStream(request.getInputStream());
        String str = null;
        while ((str = dis.readLine()) != null) {
            log.info(new String(str.getBytes("ISO-8859-1"), "utf-8") + "/n");
            // euc-kr로 전송된 한글은 깨진다.
        }
        */

        if(files.length == 0)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(fileService.upload(files, Paths.get("static")));
    }

    /**
     * 특정 경로 파일 리스트 조회
     * @param path
     * @return
     */
    @ApiOperation(value = "S3 파일 리스트 조회", notes = "S3에 저장된 특정 경로의 파일 리스트를 조회")
    @ApiImplicitParams(@ApiImplicitParam(name = "path", value = "조회할 경로", defaultValue = "static", dataType = "string"))
    @GetMapping("/list")
    public ResponseEntity<List<S3FileInfo>> getFileList(@RequestParam("path") String path) {
        List<S3FileInfo> resultList = fileService.getFileList(Paths.get(path));
        return ResponseEntity.ok(resultList);
    }

    /**
     * Resource 파일 다운로드
     * @param path
     * @param fileName
     * @return
     */
    @ApiOperation(value = "S3 파일 다운로드(Resource)", notes = "S3에서 파일을 다운로드 받음(Resource 형태로 반환)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "파일의 경로", defaultValue = "static", dataType = "string"),
            @ApiImplicitParam(name = "fileName", value = "파일명", defaultValue = "ERD.png", dataType = "string")
    })
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("path") String path, @RequestParam("fileName") String fileName) throws Exception {
        Resource resource = fileService.download(Paths.get(path, fileName), bucket);
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .cacheControl(CacheControl.noCache())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + "")
                .body(resource);
    }

    /**
     * Byte 파일 다운로드
     * @param path
     * @param fileName
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "S3 파일 다운로드(Byte)", notes = "S3에서 파일을 다운로드 받음(Byte 형태로 반환)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "파일의 경로", defaultValue = "static", dataType = "string"),
            @ApiImplicitParam(name = "fileName", value = "파일명", defaultValue = "ERD.png", dataType = "string")
    })
    @GetMapping("/bytedownload")
    public ResponseEntity<byte[]> bytedownload(@RequestParam("path") String path, @RequestParam("fileName") String fileName) throws IOException {
        return fileService.byteDownload(path, fileName);
    }

    /**
     * 단일 파일 삭제
     * @param fileName
     * @return
     */
    @ApiOperation(value = "단일 파일 삭제", notes = "S3에서 특정 파일을 삭제")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "path", value = "파일명 포함 경로", defaultValue = "static/ERD.png", dataType = "string")
    )
    @ApiResponses({@ApiResponse(code = 200, message = "삭제 성공시 S3 파일 리스트 리턴")})
    @DeleteMapping("/delete")
    public ResponseEntity<List<S3FileInfo>> delete(@RequestParam("fileName") String fileName) {
        List<S3FileInfo> resultList = fileService.delete(Paths.get(fileName));
        return ResponseEntity.ok(resultList);
    }

    /**
     * 다중 파일 삭제
     * @param fileList
     * @return
     */
    @ApiOperation(value = "다중 파일 삭제", notes = "S3에서 특정 파일들을 삭제")
    @ApiResponses({@ApiResponse(code = 200, message = "삭제 성공시 S3 파일 리스트 리턴")})
    @DeleteMapping("/deleteList")
    public ResponseEntity<List<S3FileInfo>> delete(@ApiParam(value = "파일명 포함 경로") @RequestParam("fileList") List<String> fileList) {
        List<S3FileInfo> resultList = fileService.delete(fileList, Paths.get("static"));
        return ResponseEntity.ok(resultList);
    }
}