package com.project.exception.excel;

/**
 * Created by KMS on 07/10/2019.
 * Excel 처리중 에러
 */
public class ExcelComponentException extends RuntimeException{

    private static final long serialVersionUID = 9015677010156645422L;

    public ExcelComponentException() {
        super();
    }

    public ExcelComponentException(String message) {
        super(message);
    }

    public ExcelComponentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelComponentException(Throwable cause) {
        super(cause);
    }

    protected ExcelComponentException(String message, Throwable cause,
                                      boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
