package com.project.component.excel.tutorial.reponseString.download;

import com.project.component.excel.tutorial.reponseString.StatRow;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by KMS on 29/08/2019.
 */
@Component("statXls")
public class StatXlsView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(
            Map<String, Object> model,
            Workbook workbook,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=\"stat.xls\"");

        List<StatRow> stats = (List<StatRow>) model.get("rows");

        CellStyle numberCellStyle = workbook.createCellStyle();
        DataFormat numberDataFormat = workbook.createDataFormat();
        numberCellStyle.setDataFormat(numberDataFormat.getFormat("#,##0"));

        Sheet sheet = workbook.createSheet("mobilestat");
        for (int i = 0 ; i < stats.size() ; i++) {
            StatRow stat = stats.get(i);
            Row row = sheet.createRow(i);

            Cell cell0 = row.createCell(0);
            cell0.setCellValue(stat.getName());

            Cell cell1 = row.createCell(1);
            cell1.setCellType(CellType.NUMERIC);
            cell1.setCellValue(stat.getValue1());
            cell1.setCellStyle(numberCellStyle);

            Cell cell2 = row.createCell(2);
            cell2.setCellType(CellType.NUMERIC);
            cell2.setCellValue(stat.getValue2());
            cell2.setCellStyle(numberCellStyle);
        }
    }
}
