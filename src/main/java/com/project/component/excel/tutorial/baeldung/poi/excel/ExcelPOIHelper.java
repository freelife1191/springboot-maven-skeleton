package com.project.component.excel.tutorial.baeldung.poi.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KMS on 29/08/2019.
 * 최신 .xlsx 파일 형식으로 작업 할 때는 XSSFWorkbook, XSSFSheet, XSSFRow 및 XSSFCell 클래스를 사용
 * 이전 .xls 형식으로 작업하려면 HSSFWorkbook, HSSFSheet, HSSFRow 및 HSSFCell 클래스를 사용
 */
public class ExcelPOIHelper {

    public Map<Integer, List<String>> readExcel(String fileLocation) throws IOException {

        Map<Integer, List<String>> data = new HashMap<>();
        // 파일 열기
        FileInputStream file = new FileInputStream(new File(fileLocation));
        Workbook workbook = new XSSFWorkbook(file);
        // 파일의 첫 번째 시트 검색
        Sheet sheet = workbook.getSheetAt(0);
        int i = 0;
        // 각 행을 반복
        for (Row row : sheet) {
            data.put(i, new ArrayList<String>());
            for (Cell cell : row) {
                // 셀 내용의 유형 판별
                switch (cell.getCellType()) {
                    case STRING: // getRichStringCellValue() 메소드를 사용하여 컨텐츠를 읽음
                        data.get(i)
                                .add(cell.getRichStringCellValue()
                                        .getString());
                        break;
                    case NUMERIC: // 날짜 또는 숫자를 포함 할 수 있으며 아래와 같이 읽음
                        if (DateUtil.isCellDateFormatted(cell)) {
                            data.get(i)
                                    .add(cell.getDateCellValue() + "");
                        } else {
                            data.get(i)
                                    .add((int)cell.getNumericCellValue() + "");
                        }
                        break;
                    case BOOLEAN:
                        data.get(i)
                                .add(cell.getBooleanCellValue() + "");
                        break;
                    case FORMULA:
                        data.get(i)
                                .add(cell.getCellFormula() + "");
                        break;
                    default:
                        data.get(i)
                                .add(" ");
                }
            }
            i++;
        }
        if (workbook != null){
            workbook.close();
        }
        return data;
    }

    /**
     * POI는 Excel 파일에 쓰고 JExcel보다 스타일링을 더 잘 지원함
     * @throws IOException
     */
    public void writeExcel() throws IOException {
        Workbook workbook = new XSSFWorkbook();

        try {
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

            // 컨텐츠를 현재 디렉토리의 'temp.xlsx' 파일에 쓰고 통합 문서를 닫음
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
        } finally {
            if (workbook != null) {

                workbook.close();

            }
        }
    }
}
