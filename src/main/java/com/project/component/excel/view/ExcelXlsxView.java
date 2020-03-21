package com.project.component.excel.view;

import com.project.component.excel.service.ExcelWriter;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by KMS on 02/09/2019.
 */
@Component
public class ExcelXlsxView extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {
        new ExcelWriter(workbook, model, request, response).create();
    }
}