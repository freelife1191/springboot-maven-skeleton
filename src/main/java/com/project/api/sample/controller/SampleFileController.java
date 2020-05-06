package com.project.api.sample.controller;

import com.project.api.sample.packet.ResFileSample;
import com.project.api.sample.service.SampleFileService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Sample File 컨트롤러
 * created by KMS on 03/05/2020
 */
@Slf4j
@RestController
@RequestMapping(value = "/sample/file")
@RequiredArgsConstructor
@Api(value = "Sample File API 컨트롤러", tags = "Sample File API")
public class SampleFileController {

    private final SampleFileService sampleFileService;

    @ApiOperation(value = "Sample File 업로드",notes = "한개의 파일 업로드시 사용", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({@ApiResponse(code = 200, message = "업로드 성공시 파일 업로드 파일정보 리턴")})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResFileSample> upload(@ApiParam(value = "파일", required = true) @RequestPart(value = "file") MultipartFile file) throws IOException {
        return sampleFileService.upload(file);
    }

    @ApiOperation(value = "Sample File 다운로드(Resource)", notes = "파일을 다운로드(Resource 형태로 반환)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "파일명", defaultValue = "1-1.png", dataType = "string")
    })
    @GetMapping
    public ResponseEntity<Resource> download(@RequestParam("fileName") String fileName) throws IOException {
        return sampleFileService.download(fileName);
    }
}