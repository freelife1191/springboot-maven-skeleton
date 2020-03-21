package com.project.exception.common;

/**
 * Created by KMS on 13/01/2020.
 * 파라메터 유효성 체크 실패 에러
 */
public class ParameterValidationFailedException extends RuntimeException {

    private static final long serialVersionUID = 3867646591923574363L;

    public ParameterValidationFailedException() {
    }

    public ParameterValidationFailedException(String message) {
        super(message);
    }

    public ParameterValidationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterValidationFailedException(Throwable cause) {
        super(cause);
    }

    public ParameterValidationFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
