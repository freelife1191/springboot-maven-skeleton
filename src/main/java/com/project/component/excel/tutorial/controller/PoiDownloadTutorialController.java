package com.project.component.excel.tutorial.controller;

import com.project.component.excel.constant.ExcelConstant;
import com.project.component.excel.service.ExcelConverter;
import com.project.component.excel.tutorial.service.FooService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KMS on 30/08/2019.
 * 공통 엑셀 다운로드 테스트 컨트롤러
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/tutorial/exceldownload")
public class PoiDownloadTutorialController {
    FooService fooService;
    ExcelConverter excelMapper;

    /**
     * ResponseEntity 리턴 방식 엑셀 다운로드 컨트롤러 튜토리얼
     * @param fileName
     * @return
     */
    @GetMapping("/export")
    public ResponseEntity<Resource> export(@RequestParam("fileName") String fileName) {

        ByteArrayResource resource = null;
        ByteArrayInputStream in = null;
        try {
            in = fooService.getExportInputStream(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
//                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .cacheControl(CacheControl.noCache())
                .header("Content-Disposition", "attachment; filename=users.xlsx")
                .body(new InputStreamResource(in));
    }

    /**
     * XLS 다운로드 튜토리얼 컨트롤러
     * @return
     */
    @GetMapping("excel-xls")
    public ModelAndView xlsView() {
        return new ModelAndView("excelXlsView", initExcelData());
    }

    /**
     * XLSX 다운로드 튜토리얼 컨트롤러
     * @return
     */
    @GetMapping("excel-xlsx")
    public ModelAndView xlsxView() {
        return new ModelAndView("excelXlsxView", initExcelData());
    }

    /**
     * XLSX-STREAMING 다운로드 튜토리얼 컨트롤러
     * @return
     */
    @GetMapping("excel-xlsx-streaming")
    public ModelAndView xlsxStreamingView() {
        return new ModelAndView("excelXlsxStreamingView", initExcelData());
    }

    private Map<String, Object> initExcelData() {
        Map<String, Object> map = new HashMap<>();
        map.put(ExcelConstant.FILE_NAME, "default_excel");
        map.put(ExcelConstant.TITLE, "TITLE");
        map.put(ExcelConstant.HEADER, Arrays.asList("ID", "NAME", "COMMENT"));
        map.put(ExcelConstant.BODY,
                Arrays.asList(
                        Arrays.asList("A", "a", "가"),
                        Arrays.asList("B", "b", "나"),
                        Arrays.asList("C", "c", "다")
                ));
        return map;
    }

}
