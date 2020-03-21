package com.project.exception.file;

/**
 * Created by KMS on 26/09/2019.
 * 파일 중복 체크
 */
public class FileDuplicateException extends RuntimeException {

    private static final long serialVersionUID = 6958449475647886122L;

    public FileDuplicateException() {
        super();
    }

    public FileDuplicateException(String message) {
        super(message);
    }
}
