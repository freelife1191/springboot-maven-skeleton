package com.project.exception.common;

/**
 * Created by KMS on 30/12/2019.
 * 데이터 중복 에러
 */
public class DuplicateDataException extends RuntimeException {

    private static final long serialVersionUID = 8495065360534405069L;

    public DuplicateDataException() {
    }

    public DuplicateDataException(String message) {
        super(message);
    }

    public DuplicateDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateDataException(Throwable cause) {
        super(cause);
    }

    public DuplicateDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
