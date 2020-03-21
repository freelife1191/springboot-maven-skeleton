package com.project.component.excel.constant;

/**
 * Created by KMS on 02/09/2019.
 * 엑셀 공통 기능 상수
 */
public abstract class ExcelConstant {

    public static final String FILE_NAME = "fileName";
    public static final String TITLE = "title";
    public static final String HEADER = "header";
    public static final String BODY = "body";

    public static final String COLOMN_WIDTH = "colomnWidth";
    public static final String STYLE = "style";
    public static final String AUTO_SIZING = "autoSizing";

    public static final String XLS = "xls";
    public static final String XLSX = "xlsx";
    public static final String XLSX_STREAM = "xlsx-stream";

    /* 기본 컬럼 넓이 확장 사이즈 */
    public static final int DEFAULT_COLOUMN_WIDTH = 3000;

    /* 엑셀 다운로드 상수 */
    /* XLSX-STREAMING */
    public static final String EXCEL_XLSX_STREAMING_VIEW = "excelXlsxStreamingView";

    /* XLSX */
    public static final String EXCEL_XLSX_VIEW = "excelXlsxView";

    /* XLS */
    public static final String EXCEL_XLS_VIEW = "excelXlsView";
}
