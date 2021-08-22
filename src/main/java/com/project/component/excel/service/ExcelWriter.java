package com.project.component.excel.service;

import com.project.component.excel.utils.ExcelUtils;
import com.project.exception.excel.ExcelComponentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.project.component.excel.constant.ExcelConstant.*;

/**
 * Created by KMS on 02/09/2019.
 * 엑셀 다운로드를 위한 컴포넌트
 */
@Slf4j
@RequiredArgsConstructor
public class ExcelWriter {

    private final Workbook workbook;
    private final Map<String, Object> model;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    /* 타이틀 스타일 */
    private CellStyle tStyle;
    /* 헤더 스타일 */
    private CellStyle hStyle;
    /* 바디 스타일 */
    private CellStyle bStyle;
    /* 제목 여부 */
    private boolean titleYN = false;

    private StopWatch stopWatch;

    /**
     * 엑셀 생성
     */
    public void create() {

        // 1. 파일명 셋팅
        stopWatch = new StopWatch("Excel Create");
        stopWatch.start("applyFileNameForRequest");
        applyFileNameForRequest(mapToFileName());
        stopWatch.stop();
        // 2. ContentType 셋팅
        stopWatch.start("applyContentTypeForRequest");
        applyContentTypeForRequest();
        stopWatch.stop();
        stopWatch.start("createCellStyle");
        createCellStyle();
        stopWatch.stop();
        // 3. 시트 생성
        stopWatch.start("createSheet");
        Sheet sheet = workbook.createSheet();
        //AutoSizing 옵션 사용시 엑셀쓰기가 10배가 느려져서 제거함
        //((SXSSFSheet) sheet).trackAllColumnsForAutoSizing(); //excelXlsxStreamingView AutoSize 조절시 꼭 설정해야함
        stopWatch.stop();
        // 4. 제목 생성
        stopWatch.start("createTitle");
        createTitle(sheet, stringToTitle(), mapToHeaderList());
        stopWatch.stop();
        // 5. 헤더 생성
        stopWatch.start("createHeader");
        createHeader(sheet, mapToHeaderList());
        stopWatch.stop();
        // 6. 바디 생성
        stopWatch.start("createBody");
        createBody(sheet, mapToBodyList());
        stopWatch.stop();
        // 7. 엑셀 파일 생성 및 업로드
        //createFile();

        log.debug("Excel Create shortSummary : {}",stopWatch.shortSummary());
        log.debug("Excel Create totalTimeMillis: {}",stopWatch.getTotalTimeMillis());
        log.debug("Excel Create prettyPrint : {}",stopWatch.prettyPrint());
    }

    /**
     * 파일명 설정
     * @return
     */
    private String mapToFileName() {
        return (String) model.get(FILE_NAME);
    }

    /**
     * 제목 설정
     * @return
     */
    private String stringToTitle() {
        return (String) model.get(TITLE);
    }

    /**
     * 헤더 설정
     * @return
     */
    private List<String> mapToHeaderList() {
        return (List<String>) model.get(HEADER);
    }

    /**
     * 바디 설정
     * @return
     */
    private List<List<String>> mapToBodyList() {
        return (List<List<String>>) model.get(BODY);
    }

    /**
     * 파일명 설정 적용
     * @param fileName
     */
    private void applyFileNameForRequest(String fileName) {
        /*
        try {
            // fileName = URLEncoder.encode(fileName, "EUC-KR");
            fileName = new String(fileName.getBytes( "KSC5601"), "8859_1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        */
        /*
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        log.info("userAgent.getBrowser().getGroup() = {}, fileName = {}",userAgent.getBrowser().getGroup(), fileName);
        String encodedFileName = FileNameEncoder.encode(userAgent.getBrowser().getGroup(), fileName);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + appendFileExtension(encodedFileName) + "\"");
        */

         try {
             log.info("Excel File Created:: fileName = {}.xlsx",fileName);

             fileName = StringUtils.isNotEmpty(fileName) ? ExcelUtils.getDisposition(fileName, ExcelUtils.getBrowser(request)) : "export";
             fileName = appendFileExtension(fileName);
         } catch (Exception e) {
             throw new ExcelComponentException(e.getMessage(), e);
         }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
    }

    /**
     * 파일 확장자 설정 적용
     * @param fileName
     * @return
     */
    private String appendFileExtension(String fileName) {
        if (workbook instanceof XSSFWorkbook || workbook instanceof SXSSFWorkbook) {
            fileName += ".xlsx";
        }
        if (workbook instanceof HSSFWorkbook) {
            fileName += ".xls";
        }

        return fileName;
    }

    /**
     * 요청 ContentType 적용
     */
    private void applyContentTypeForRequest() {
        if (workbook instanceof XSSFWorkbook || workbook instanceof SXSSFWorkbook) {
            response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        }
        if (workbook instanceof HSSFWorkbook) {
            // response.setHeader("Content-Type", "application/vnd.ms-excel;charset=EUC-KR");
            response.setHeader("Content-Type", "application/vnd.ms-excel;charset=UTF-8");
        }
    }

    /**
     * 제목 생성
     * @param sheet
     * @param title
     */
    private void createTitle(Sheet sheet, String title, List<String> headList) {
        if(StringUtils.isNotEmpty(title)) {

            int colSize = headList.size();

            if(colSize > 5) colSize = 5;

            //셀 병합
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,colSize));
            //헤더 로우 생성
            createRow(sheet, title, 0, tStyle);
            this.titleYN = true;
        }
    }

    /**
     * 헤더 생성
     * @param sheet
     * @param headList
     */
    private void createHeader(Sheet sheet, List<String> headList) {
        int rowNum = titleYN ? 1 : 0;

        if(headList != null) {
            int size = headList.size();
            Row row = sheet.createRow(rowNum);

            for (int i = 0; i < size; i++) {
                row.createCell(i).setCellValue(headList.get(i) != null ? headList.get(i) : " ");
                if((boolean) model.get(STYLE))
                    row.getCell(i).setCellStyle(hStyle); // 스타일 적용

                if((boolean) model.get(AUTO_SIZING))
                    sheet.autoSizeColumn(i); //컬럼 width  사이즈 보기좋게 확장
                else {
                    //컬럼 사이즈 적용 사용자 정의 설정이 없으면 기본 설정 적용
                    Integer colomnWidth = model.get(COLOMN_WIDTH) != null ? (Integer) model.get(COLOMN_WIDTH) : DEFAULT_COLOUMN_WIDTH;
                    sheet.setColumnWidth(i, colomnWidth);
                }
            }
        }
    }

    /**
     * 바디 생성
     * @param sheet
     * @param bodyList
     */
    private void createBody(Sheet sheet, List<List<String>> bodyList) {
        if(bodyList != null) {
            int rowSize = bodyList.size();
            int rowNumExtendValue = titleYN ? 2 : 1;
            for (int i = 0; i < rowSize; i++) {
                createRow(sheet, bodyList.get(i), i + rowNumExtendValue, bStyle);
                //log.info("i = {}, rowNumExtendValue = {}",i + rowNumExtendValue,rowNumExtendValue);

                //AutoSizing 옵션 사용시 엑셀쓰기가 10배가 느려져서 제거함
                //if( i == rowNumExtendValue && (boolean) model.get(AUTO_SIZING)) sheet.autoSizeColumn(rowNumExtendValue); //첫 번째 ROW 의 컬럼 width 만 사이즈 보기좋게 확장

                //1000 로우 마다 메모리를 비워줌
                /*
                if(i % 1000 == 0) {
                    try {
                        ((SXSSFSheet) sheet).flushRows(1000);
                    } catch (IOException e) {
                        throw new ExcelComponentException(e.getMessage(),e);
                    }
                }
                */
            }
        }
    }

    /**
     * 셀 스트링 데이터 생성
     * @param sheet
     * @param cellData
     * @param rowNum
     */
    private void createRow(Sheet sheet, String cellData, int rowNum, CellStyle style) {

        List<String> cellList = cellData != null ? Arrays.asList(cellData) : null;

        createRow(sheet, cellList, rowNum, style);
    }

    /**
     * 셀 리스트 데이터 생성
     * @param sheet
     * @param cellList
     * @param rowNum
     */
    private void createRow(Sheet sheet, List<String> cellList, int rowNum, CellStyle style) {
        if(cellList != null) {
            int size = cellList.size();
            Row row = sheet.createRow(rowNum);

            for (int i = 0; i < size; i++) {
                row.createCell(i).setCellValue(cellList.get(i) != null ? cellList.get(i) : " ");
                if((boolean) model.get(STYLE))
                    row.getCell(i).setCellStyle(style); //스타일 적용
            }
        }

    }

    /**
     * 셀 스타일 설정
     */
    private void createCellStyle() {

        /** 타이틀 스타일 선언 */
        tStyle = workbook.createCellStyle();
        /** 헤더 스타일 */
        tStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        tStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        tStyle.setBorderBottom(BorderStyle.THIN);
        tStyle.setBorderLeft(BorderStyle.THIN);
        tStyle.setBorderRight(BorderStyle.THIN);
        tStyle.setBorderTop(BorderStyle.THIN);
        tStyle.setAlignment(HorizontalAlignment.CENTER);
        tStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        /** 헤더 FONT 셋팅  */
        Font titleFont = workbook.createFont();
        titleFont.setFontName("맑은 고딕"); // 폰트 이름
        titleFont.setFontHeightInPoints((short) 15);
        titleFont.setBold(true);
        tStyle.setFont(titleFont);


        /** 헤더 스타일 선언 */
        hStyle = workbook.createCellStyle();
        /** 헤더 스타일 */

        hStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        hStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        hStyle.setBorderBottom(BorderStyle.THIN);
        hStyle.setBorderLeft(BorderStyle.THIN);
        hStyle.setBorderRight(BorderStyle.THIN);
        hStyle.setBorderTop(BorderStyle.THIN);
        hStyle.setAlignment(HorizontalAlignment.CENTER);

        /** 헤더 FONT 셋팅  */
        Font headerFont = workbook.createFont();
        headerFont.setFontName("맑은 고딕"); // 폰트 이름
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(false);
        hStyle.setFont(headerFont);

        /** 바디 스타일 선언 */
        bStyle = workbook.createCellStyle();
        /** 바디 스타일 */

        bStyle.setBorderBottom(BorderStyle.THIN);
        bStyle.setBorderLeft(BorderStyle.THIN);
        bStyle.setBorderRight(BorderStyle.THIN);
        bStyle.setBorderTop(BorderStyle.THIN);
        bStyle.setAlignment(HorizontalAlignment.LEFT);
        bStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        /** 헤더 FONT 셋팅  */
        Font bodyFont = workbook.createFont();
        bodyFont.setFontName("맑은 고딕"); // 폰트 이름
        bodyFont.setFontHeightInPoints((short) 10);
        bodyFont.setBold(false);
        bStyle.setFont(bodyFont);
    }

    /**
     * 엑셀 파일 생성 및 업로드
     */
    private void createFile() {
        log.info("#response Name : "+this.response.getHeader("Content-Disposition"));

        //원본 파일명
        String fileName = "export.xlsx";
        log.debug(" FileName : "+fileName);

        //파일 확장자명(소문자변환)
        String fileExtension = FilenameUtils.getExtension(fileName).toLowerCase();
        log.debug(" fileExtension : "+fileExtension);

        File uploadFile;
        String uploadFileName;

        do {
            //업로드패스 (ROOT패스 + UPLOAD패스 + UPLOAD파일명)
            String uploadPath = "target/files/" + fileName;
            log.debug("uploadFilePath : "+uploadPath);

            //업로드 파일 생성
            uploadFile = new File(uploadPath);
        } while (uploadFile.exists());
        //업로드 폴더 생성
        uploadFile.getParentFile().mkdirs();

        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(uploadFile);
        } catch (FileNotFoundException e) {
            throw new ExcelComponentException(e.getMessage(), e);
        }

        try {
            this.workbook.write(fileOut);
        } catch (IOException e) {
            throw new ExcelComponentException(e.getMessage(), e);
        }
    }
}
