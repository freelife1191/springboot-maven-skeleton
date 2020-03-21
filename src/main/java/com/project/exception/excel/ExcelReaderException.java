package com.project.exception.excel;

/**
 * 엑셀 업로드 시 캐치 하지 못한 그외 에러
 * Created by KMS on 17/03/2020.
 */
public class ExcelReaderException extends RuntimeException {

    private static final long serialVersionUID = -4388918545286482493L;

    public ExcelReaderException() {
    }

    public ExcelReaderException(String message) {
        super(message);
    }

    public ExcelReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelReaderException(Throwable cause) {
        super(cause);
    }

    public ExcelReaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
