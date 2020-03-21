package com.project.exception.file;

/**
 * Created by KMS on 07/10/2019.
 * 파일 데이터가 존재 하지 않을때
 */
public class FileNotExistException extends RuntimeException{

    private static final long serialVersionUID = 2355889413018616016L;

    public FileNotExistException() {
        super();
    }

    public FileNotExistException(String message) {
        super(message);
    }
}
