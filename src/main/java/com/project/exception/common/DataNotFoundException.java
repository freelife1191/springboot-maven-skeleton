package com.project.exception.common;

/**
 * 데이터가 존재하지 않을때 예외처리
 * Created by KMS on 29/01/2020.
 */
public class DataNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3609348793420954762L;

    public DataNotFoundException() {
    }

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataNotFoundException(Throwable cause) {
        super(cause);
    }

    public DataNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
