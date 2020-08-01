package com.project.exception.file;

import lombok.Getter;
import lombok.ToString;

/**
 * 파일 다운로드 실패 예외
 * Created by KMS on 30/07/2020.
 */
@Getter
@ToString
public class FileDownloadFailException extends RuntimeException {

    private static final long serialVersionUID = -2440647007995074773L;

    public FileDownloadFailException() {
    }

    public FileDownloadFailException(String message) {
        super(message);
    }

    public FileDownloadFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDownloadFailException(Throwable cause) {
        super(cause);
    }

    public FileDownloadFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
