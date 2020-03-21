package com.project.exception.common;

/**
 * Created by KMS on 16/12/2019.
 * 응답에 문제가 있을시 처리하는 Exception
 */
public class ResponseProblemException extends RuntimeException {

    private static final long serialVersionUID = 5220473370147708496L;

    public ResponseProblemException() {
    }

    public ResponseProblemException(String message) {
        super(message);
    }

    public ResponseProblemException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseProblemException(Throwable cause) {
        super(cause);
    }

    public ResponseProblemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
