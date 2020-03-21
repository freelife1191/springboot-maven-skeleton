package com.project.api.sample.service;

import com.project.api.sample.dto.SampleExcelDto;
import com.project.common.domain.CommonResult;
import com.project.component.excel.service.ExcelReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.project.common.constant.ResCode.SUCCESS;

/**
 * Sample Excel 서비스
 * Created by KMS on 18/03/2020.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SampleExcelService {

    private final ResourceLoader resourceLoader;

    /**
     * Sample 엑셀 업로드 테스트
     * @param jwt
     * @param file
     * @return
     * @throws Exception
     */
    public CommonResult<List<SampleExcelDto>> excelUpload(String jwt, MultipartFile file) throws Exception {

        List<SampleExcelDto> sampleExcelDtoList = ExcelReader.getObjectList(file, SampleExcelDto::from);
        for(SampleExcelDto dto: sampleExcelDtoList) {
            log.info("Excel Upload Sample Data = {}", dto);
        }
        return new CommonResult<>(SUCCESS, SUCCESS.getMessage(), sampleExcelDtoList);
    }

    /**
     * Sample 엑셀 다운로드
     * @return
     * @throws IOException
     */
    public ResponseEntity<Resource>  getSampleExcel() throws IOException {

        String fileName = "sampleUpload.xlsx";

        // 리소스 파일 획득하기
        // /src/main/resources/something.txt 파일을 읽어 온다.
        // String something = IOUtils.toString(getClass().getResourceAsStream("/something.txt"), "UTF-8");

        //Path filePath = Paths.get("src", "resources", "file", File.separatorChar + fileName);
        // Resource resource = resourceLoader.getResource(filePath.toString());
        Path filePath = Paths.get(File.separatorChar + "file", File.separatorChar + fileName);
        Resource resource = new InputStreamResource(getClass().getResourceAsStream(filePath.toString()));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .cacheControl(CacheControl.noCache())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + "")
                .body(resource);
    }
}
