package com.project.api.sample.controller;

import com.project.api.sample.packet.ReqFileSample;
import com.project.api.sample.packet.ReqJsonFileSample;
import com.project.api.sample.packet.ResFileSample;
import com.project.api.sample.service.SampleFileService;
import com.project.common.domain.CommonResult;
import com.project.utils.common.CommonUtils;
import com.project.utils.common.JsonUtils;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

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

    @ApiOperation(value = "Sample File 업로드 기본",notes = "한개의 파일 업로드시 사용")//, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Content-Type", defaultValue = "multipart/form-data", paramType = "header")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "업로드 성공시 파일 업로드 파일정보 리턴")})
    @PostMapping//(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResFileSample> upload(@ApiParam(value = "파일", required = true) @RequestPart(value = "file") MultipartFile file, HttpServletRequest request, WebRequest webRequest) throws IOException {
        //ExceptionHandler로 RequestBody 값을 전달해주기 위한 셋팅
        webRequest.setAttribute("body", file, RequestAttributes.SCOPE_REQUEST);
        log.info("body = {}",file);
        return sampleFileService.upload(file, request, webRequest);
    }

    @ApiOperation(value = "Sample File 업로드 ModelAttribute",notes = "한개의 파일 업로드와 추가적인 파라메터를 넘길때", consumes = MediaType.MULTIPART_FORM_DATA_VALUE ) //, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Content-Type", defaultValue = MediaType.MULTIPART_FORM_DATA_VALUE, paramType = "header")// +","+MediaType.MULTIPART_MIXED_VALUE, paramType = "header")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "업로드 성공시 파일 업로드 파일정보 리턴")})
    @PostMapping(value = "/mix")//, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})//, MediaType.MULTIPART_MIXED_VALUE})
    public ResponseEntity<ResFileSample> mixUpload(@ApiParam(value = "파일 객체", required = true)
                                                   @ModelAttribute ReqFileSample reqFileSample, HttpServletRequest request, WebRequest webRequest) throws IOException {
        //ExceptionHandler로 RequestBody 값을 전달해주기 위한 셋팅
        webRequest.setAttribute("body", reqFileSample, RequestAttributes.SCOPE_REQUEST);
        log.info("body = {}",reqFileSample);
        return sampleFileService.mixUpload(reqFileSample, request, webRequest);
    }

    @ApiOperation(value = "Sample File 업로드 ModelAttribute CommonResult 리턴",notes = "여러개의 파일 업로드와 추가적인 파라메터를 넘길때", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) //, produces = MediaType.APPLICATION_JSON_VALUE)

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Content-Type", defaultValue = MediaType.MULTIPART_FORM_DATA_VALUE, paramType = "header")// +","+MediaType.MULTIPART_MIXED_VALUE, paramType = "header")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "업로드 성공시 파일 업로드 파일정보 리턴")})
    @PostMapping(value = "/mixmap")//, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})//, MediaType.MULTIPART_MIXED_VALUE})
    public CommonResult<?> mixMapUpload(@ApiParam(value = "파일 객체", required = true)
                                        @ModelAttribute ReqFileSample reqFileSample, HttpServletRequest request, WebRequest webRequest) throws Exception {
        //ExceptionHandler로 RequestBody 값을 전달해주기 위한 셋팅
        webRequest.setAttribute("body", reqFileSample, RequestAttributes.SCOPE_REQUEST);
        log.info("body = {}",reqFileSample);
        return sampleFileService.mixMapUpload(reqFileSample, request, webRequest);
    }

    @ApiOperation(value = "Sample File 업로드 JSON",notes = "한개의 파일 업로드와 추가적인 파라메터를 넘길때", consumes = MediaType.MULTIPART_FORM_DATA_VALUE+","+MediaType.MULTIPART_MIXED_VALUE) //, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Content-Type", defaultValue = MediaType.MULTIPART_FORM_DATA_VALUE+","+MediaType.MULTIPART_MIXED_VALUE, paramType = "header")// +","+MediaType.MULTIPART_MIXED_VALUE, paramType = "header")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "업로드 성공시 파일 업로드 파일정보 리턴")})
    @PostMapping(value = "/mixjson", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.MULTIPART_MIXED_VALUE})//, MediaType.MULTIPART_MIXED_VALUE})
    public CommonResult<?> mixJsonMapUpload(@ApiParam(value = "파일", required = true) @RequestPart(value = "file") MultipartFile file,
                                            @ApiParam(value = "요청 JSON", required = false) @RequestParam("params") String params,
                                            // @ApiParam(value = "이름", required = false) @RequestParam("name") String name,
                                            HttpServletRequest request, WebRequest webRequest) throws Exception {
        ReqJsonFileSample jsonData = JsonUtils.getObjectMapper().readValue(params, ReqJsonFileSample.class);
        log.info("## jsonData = {}",jsonData);
        ReqFileSample reqFileSample = new ReqFileSample();
        CommonUtils.parentObjectToDto(jsonData, reqFileSample);
        reqFileSample.setFile(file);
        return sampleFileService.mixJsonMapUpload(reqFileSample, request, webRequest);
    }

    @ApiOperation(value = "Sample File 다중 업로드 기본",notes = "여러개의 파일 업로드시 사용")//, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    /*
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Content-Type", defaultValue = "multipart/form-data", paramType = "header")
    })
    */
    @ApiResponses({@ApiResponse(code = 200, message = "업로드 성공시 파일 업로드 파일정보 리턴")})
    @PostMapping("/multi")//(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ResFileSample>> multiUpload(@ApiParam(value = "파일 리스트", required = true)
                                                           @RequestPart(value = "files") MultipartFile[] files, HttpServletRequest request, WebRequest webRequest) throws IOException {
        //ExceptionHandler로 RequestBody 값을 전달해주기 위한 셋팅
        webRequest.setAttribute("body", files, RequestAttributes.SCOPE_REQUEST);
        log.info("body = {}",files);
        return sampleFileService.multiUpload(files, request, webRequest);
    }

    @ApiOperation(value = "Sample File 다중 업로드 ModelAttribute CommonResult 리턴",notes = "한개의 파일 업로드와 추가적인 파라메터를 넘길때", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) //, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Content-Type", defaultValue = MediaType.MULTIPART_FORM_DATA_VALUE, paramType = "header")// +","+MediaType.MULTIPART_MIXED_VALUE, paramType = "header")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "업로드 성공시 파일 업로드 파일정보 리턴")})
    @PostMapping(value = "/multi/mixmap")//, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})//, MediaType.MULTIPART_MIXED_VALUE})
    public CommonResult<?> mixMapMultiUpload(@ApiParam(value = "파일 객체", required = true)
                                             @ModelAttribute ReqFileSample reqFileSample, HttpServletRequest request, WebRequest webRequest) throws Exception {
        //ExceptionHandler로 RequestBody 값을 전달해주기 위한 셋팅
        webRequest.setAttribute("body", reqFileSample, RequestAttributes.SCOPE_REQUEST);
        log.info("body = {}",reqFileSample);
        return sampleFileService.mixMapMultiUpload(reqFileSample, request, webRequest);
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