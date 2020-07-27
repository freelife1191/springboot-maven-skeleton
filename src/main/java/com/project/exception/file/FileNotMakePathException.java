package com.project.exception.file;

/**
 * 파일 경로 생성 실패
 * Created by KMS on 23/07/2020.
 */
public class FileNotMakePathException extends RuntimeException {

    private static final long serialVersionUID = -5767058462722790068L;

    public FileNotMakePathException() {
    }

    public FileNotMakePathException(String message) {
        super(message);
    }

    public FileNotMakePathException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileNotMakePathException(Throwable cause) {
        super(cause);
    }

    public FileNotMakePathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
