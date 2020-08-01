package com.project.exception.file;

/**
 * 파일 삭제 실패 에러
 * Created by KMS on 30/07/2020.
 */
public class FileDeleteFailException extends RuntimeException {

    private static final long serialVersionUID = -8322738112950966947L;

    public FileDeleteFailException() {
        super();
    }

    public FileDeleteFailException(String message) {
        super(message);
    }

    public FileDeleteFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDeleteFailException(Throwable cause) {
        super(cause);
    }

    protected FileDeleteFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
