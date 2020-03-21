package com.project.exception.custom;

/**
 * Created by KMS on 26/09/2019.
 * 커스텀 Exception
 */
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = -4545285536925716473L;

    public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    public CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
