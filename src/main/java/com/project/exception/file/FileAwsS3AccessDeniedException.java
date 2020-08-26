package com.project.exception.file;

/**
 * Created by KMS on 25/08/2020.
 * AWS S3 처리중 권한 에러에 대한 처리
 */
public class FileAwsS3AccessDeniedException extends RuntimeException {

    private static final long serialVersionUID = 3857404267550246254L;

    public FileAwsS3AccessDeniedException() {
    }

    public FileAwsS3AccessDeniedException(String message) {
        super(message);
    }

    public FileAwsS3AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileAwsS3AccessDeniedException(Throwable cause) {
        super(cause);
    }

    public FileAwsS3AccessDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
