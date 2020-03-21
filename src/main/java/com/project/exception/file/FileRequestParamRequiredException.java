package com.project.exception.file;

/**
 * Created by KMS on 25/09/2019.
 */
public class FileRequestParamRequiredException extends RuntimeException {

    private static final long serialVersionUID = 5466524715629238130L;

    public FileRequestParamRequiredException() {
        super();
    }

    public FileRequestParamRequiredException(String message) {
        super(message);
    }
}
