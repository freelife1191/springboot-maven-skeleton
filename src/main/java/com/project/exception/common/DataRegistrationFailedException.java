package com.project.exception.common;

/**
 * 데이터 등록 실패 예외 처리
 * Created by HJB on 2020-02-04.
 */
public class DataRegistrationFailedException extends RuntimeException {

    private static final long serialVersionUID = 818329688187771460L;

    public DataRegistrationFailedException() {
    }

    public DataRegistrationFailedException(String message) {
        super(message);
    }

    public DataRegistrationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataRegistrationFailedException(Throwable cause) {
        super(cause);
    }

    public DataRegistrationFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
