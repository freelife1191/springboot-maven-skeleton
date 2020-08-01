package com.project.exception.file;

import lombok.Getter;
import lombok.ToString;

/**
 * Created by KMS on 07/10/2019.
 * 파일 데이터가 존재 하지 않을때
 */
@Getter
@ToString
public class FileNotExistException extends RuntimeException{

    private static final long serialVersionUID = 2355889413018616016L;

    public FileNotExistException() {
        super();
    }

    public FileNotExistException(String message) {
        super(message);
    }

    public FileNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileNotExistException(Throwable cause) {
        super(cause);
    }

    protected FileNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
