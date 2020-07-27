package com.project.exception.file;

/**
 * Created by KMS on 07/10/2019.
 * AWS S3 처리중 에러 처리
 */
public class FileAwsS3ProcessException extends RuntimeException{

    private static final long serialVersionUID = 3637999822893128862L;

    public FileAwsS3ProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileAwsS3ProcessException(Throwable cause) {
        super(cause);
    }

    protected FileAwsS3ProcessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FileAwsS3ProcessException() {
        super();
    }

    public FileAwsS3ProcessException(String message) {
        super(message);
    }
}
