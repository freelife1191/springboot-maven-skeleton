package com.project.exception.common;

/**
 * 지원되지 않는 기능에 대한 예외처리
 * Created by KMS on 29/08/2020.
 */
public class NotSupportedException extends RuntimeException {

    private static final long serialVersionUID = 6769245994634661024L;

    public NotSupportedException() {
        super();
    }

    public NotSupportedException(String message) {
        super(message);
    }

    public NotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportedException(Throwable cause) {
        super(cause);
    }

    protected NotSupportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
