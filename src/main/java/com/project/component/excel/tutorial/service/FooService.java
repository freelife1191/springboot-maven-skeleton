package com.project.component.excel.tutorial.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by KMS on 30/08/2019.
 */
@Service
public class FooService {

    public ByteArrayInputStream getExportInputStream(String fileName) throws IOException {

        byte[] bytes = new byte[1024];
        try(Workbook workbook = generateExcel(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }

    }

    private FileOutputStream write(Workbook workbook, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        workbook.write(fos);
        fos.close();
        return fos;
    }

    private Workbook generateExcel() throws IOException {
        Workbook workbook = new XSSFWorkbook();

        // 사람 목록을 작성
        Sheet sheet = workbook.createSheet("Persons");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();

        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        // 이름과 나이 셀이 포함된 헤더 행 생성하고 스타일 지
        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Age");
        headerCell.setCellStyle(headerStyle);

        // 테이블의 내용을 다른 스타일로 작성
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        Row row = sheet.createRow(2);
        Cell cell = row.createCell(0);
        cell.setCellValue("John Smith");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue(20);
        cell.setCellStyle(style);

        return workbook;
    }

}
