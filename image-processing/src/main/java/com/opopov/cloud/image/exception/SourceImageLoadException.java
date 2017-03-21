package com.opopov.cloud.image.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by elx01 on 3/20/17.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SourceImageLoadException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SourceImageLoadException(String message, Throwable causedBy) {
        super(message, causedBy);
    }
}