package com.project.exception.file;

/**
 * Created by KMS on 25/09/2019.
 */
public class FileRequestFileNotException extends RuntimeException {

    private static final long serialVersionUID = 6942953164217818321L;

    public FileRequestFileNotException() {
        super();
    }

    public FileRequestFileNotException(String message) {
        super(message);
    }

}
