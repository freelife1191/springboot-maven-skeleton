package com.project.exception.excel;

/**
 * 엑셀 업로드 데이터 타입 에러
 * Created by KMS on 17/03/2020.
 */
public class ExcelReaderTypeException extends RuntimeException {

    private static final long serialVersionUID = -4064494582933227967L;

    public ExcelReaderTypeException() {
    }

    public ExcelReaderTypeException(String message) {
        super(message);
    }

    public ExcelReaderTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelReaderTypeException(Throwable cause) {
        super(cause);
    }

    public ExcelReaderTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
