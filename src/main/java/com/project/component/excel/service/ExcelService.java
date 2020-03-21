package com.project.component.excel.service;


import com.project.component.excel.domain.ReqExcelDownload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.project.component.excel.constant.ExcelConstant.EXCEL_XLSX_STREAMING_VIEW;

/**
 * Created by KMS on 09/09/2019.
 * 엑셀 서비스
 */
@Slf4j
@Service
public class ExcelService {

    final ExcelConverter excelConverter;

    public ExcelService(ExcelConverter excelConverter) {
        this.excelConverter = excelConverter;
    }

    /**
     * 엑셀 다운로드 서비스
     * Map 이나 객체 형태로 받아서 엑셀 파일로 만들어 리턴함
     * 엑셀 다운로드 서비스를 이용하기 위해서 요청 파라메터는
     * ReqExcel 엑셀 객체를 상속 받아서 파라메터로 전달해야함
     * @param list
     * @param <T>
     * @return
     */
    public <T> ModelAndView download(List<T> list) {

        return new ModelAndView(EXCEL_XLSX_STREAMING_VIEW, excelConverter.convertList(list));

    }

    /**
     * 엑셀 다운로드 서비스
     * Map 이나 객체 형태로 받아서 엑셀 파일로 만들어 리턴함
     * 엑셀 다운로드 서비스를 이용하기 위해서 요청 파라메터는
     * ReqExcel 엑셀 객체를 상속 받아서 파라메터로 전달해야함
     * @param list
     * @param <T> 엑셀 변환 데이터
     * @param <E extends ReqExcel> 엑셀 파라메터
     * @return
     */
    public <T, E extends ReqExcelDownload> ModelAndView download(List<T> list, E req) {

        return new ModelAndView(EXCEL_XLSX_STREAMING_VIEW, excelConverter.convertList(list, req));

    }
}
