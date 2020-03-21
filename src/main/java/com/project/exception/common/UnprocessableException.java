package com.project.exception.common;

/**
 * Created by KMS on 12/12/2019.
 * 처리 불가 에러 핸들러
 */
public class UnprocessableException extends RuntimeException {

    private static final long serialVersionUID = 5642093248429668195L;

    public UnprocessableException() {
    }

    public UnprocessableException(String message) {
        super(message);
    }

    public UnprocessableException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnprocessableException(Throwable cause) {
        super(cause);
    }

    public UnprocessableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
