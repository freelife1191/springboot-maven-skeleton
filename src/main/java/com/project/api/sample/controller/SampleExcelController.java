package com.project.api.sample.controller;

import com.project.api.sample.dto.SampleExcelDto;
import com.project.api.sample.packet.ReqJsonSample;
import com.project.api.sample.service.SampleExcelService;
import com.project.common.domain.CommonResult;
import com.project.component.excel.model.ExcelReaderErrorField;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Sample Excel 컨트롤러
 * Created by KMS on 18/03/2020.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sample")
@Api(value = "Sample Excel API 컨트롤러", tags = "Sample Excel API")
// @ApiIgnore
public class SampleExcelController {

    private final SampleExcelService sampleExcelService;

    @ApiOperation(value = "Sample 엑셀 양식 다운로드")
    @GetMapping("/excel")
    public ResponseEntity<Resource> getSampleExcel() throws IOException {
        return sampleExcelService.getSampleExcel();
    }

    @ApiOperation(value = "Sample 엑셀 업로드", nickname = "KMS", notes = "# Sample 엑셀 업로드를 테스트 해볼 수 있는 API\n" +
            "## 엑셀 업로드 공통 서비스 가이드\n" +
            "---\n" +
            "https://\n\n" +
            "## 사용예시\n" +
            "---\n" +
            "`List<SampleExcelDto> dataList = ExcelReader.getObjectList(multipartFile, SampleExcelDto::from);`")
    @ApiResponses({
            @ApiResponse(code = 4010, message = "읽을 수 없는 엑셀 파일 입니다 (DRM 적용 또는 다른이유)"),
            @ApiResponse(code = 4011, message = "엑셀 업로드 FIELD ERROR :: 입력 데이터를 확인하세요\n" +
                    "## Sample Data\n" +
                    "### TYPE\n" +
                    "___\n" +
                    "`TYPE`: 잘못된 데이터 타입\n" +
                    "`EMPTY`: 필수 입력값 누락\n" +
                    "`VALID`: 유효성 검증 실패\n" +
                    "`UNKNOWN`: 알수 없는 에러\n" +
                    "```json" +
                    "" +
                    "'[\n" +
                    "[\n"+
                    "  {\n" +
                    "    \"type\": \"EMPTY\",\n" +
                    "    \"row\": 2,\n" +
                    "    \"field\": \"name\",\n" +
                    "    \"fieldHeader\": \"이름\",\n" +
                    "    \"inputData\": \"\",\n" +
                    "    \"message\": \"필수 입력값 누락\",\n" +
                    "    \"exceptionMessage\": \"이름은 필수 입력값입니다\"\n" +
                    "  }\n" +
                    "]"+
                    "",
                    response = ExcelReaderErrorField.class, responseContainer = "List",
                    examples = @Example(value = {
                            @ExampleProperty(
                                    value = "[\n"+
                                            "  {\n" +
                                            "    \"type\": \"EMPTY\",\n" +
                                            "    \"row\": 2,\n" +
                                            "    \"field\": \"name\",\n" +
                                            "    \"fieldHeader\": \"이름\",\n" +
                                            "    \"inputData\": \"\",\n" +
                                            "    \"message\": \"필수 입력값 누락\",\n" +
                                            "    \"exceptionMessage\": \"이름은 필수 입력값입니다\"\n" +
                                            "  }\n" +
                                            "]",
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    })
            ),
    })
    @PostMapping(value = "/excel", consumes = { MediaType.MULTIPART_MIXED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResult<List<SampleExcelDto>> insertEquipment(@ApiParam("JWT 토큰 - 요청시 전달 받아 ID를 추출한다")
                                                              @RequestHeader(value = "jwt", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbktleSI6IjIxMEJGMDY1LUFDRjMtNEUxQS04QTM4LThFRjQ0Mzg1NTM5QSIsImlkIjoieXNraW0iLCJuYW1lIjoiXHVhZTQwXHVjNzIwXHVjMTFkIiwiZGVwYXJ0bWVudF9jb2RlIjoiMTUwMTIiLCJkZXBhcnRtZW50X25hbWUiOiJcdWM2ZjlcdWQ1MDRcdWI4NjBcdWQyYjhcdWM1ZDRcdWI0ZGNcdWQzMGNcdWQyYjgiLCJlbXBsb3llZV9udW1iZXIiOiIyMDE3MDAyNCIsImVtYWlsIjoieXNraW1AaXBhcmtpbmcuY28ua3IiLCJwb3NpdGlvbl9jb2RlIjoiSjEiLCJwb3NpdGlvbl9uYW1lIjoiXHViOWU0XHViMmM4XHVjODAwIiwiZHV0eV9jb2RlIjoiTDIiLCJkdXR5X25hbWUiOiJcdWQzMDBcdWM2ZDAiLCJ0ZWFtX25hbWUiOm51bGwsInBhdGgiOiIxMDAwfDEwMDAwfDE1MDExfDE1MDEyIiwicGF0aF9uYW1lIjoiXHVkMzBjXHVkMGI5XHVkMDc0XHViNzdjXHVjNmIwXHViNGRjfFImRHxcdWQ1MDRcdWI4NjBcdWQyYjhcdWM1ZDRcdWI0ZGNcdWQzMDB8XHVjNmY5XHVkNTA0XHViODYwXHVkMmI4XHVjNWQ0XHViNGRjXHVkMzBjXHVkMmI4In0.bxBABM7tSoHyxMyfp2P89rQSGS_cWpAqc-1bcGDrbrY") String jwt,
                                                              @ApiParam("업로드 엑셀파일 - 업무에 해당하는 엑셀 업로드 양식을 다운 받아 양식에 맞게 작성한뒤 업로드해야함")
                                                              @RequestPart(value = "file") MultipartFile file) throws Exception {
        return sampleExcelService.excelUpload(jwt, file);
    }

    @ApiOperation(value = "Sample 엑셀 업로드 테스트", nickname = "KMS")
    @PostMapping(value = "/excel", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public CommonResult<List<ReqJsonSample>> excelUpload(@Valid @RequestBody List<ReqJsonSample> samples,
                                                         WebRequest webRequest) throws Exception {
        return sampleExcelService.excelUpload(samples);
    }
}
