package com.project.exception.common;

/**
 * AES 암호화 에러 예외처리
 * Created by KMS on 03/03/2020.
 */
public class AesEncryptException extends RuntimeException {

    private static final long serialVersionUID = 3406045244931425832L;

    public AesEncryptException() {
    }

    public AesEncryptException(String message) {
        super(message);
    }

    public AesEncryptException(String message, Throwable cause) {
        super(message, cause);
    }

    public AesEncryptException(Throwable cause) {
        super(cause);
    }

    public AesEncryptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
