package com.opopov.cloud.image.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by elx01 on 3/20/17.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ImageWriteException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ImageWriteException(String message, Exception causedBy) {
        super(message, causedBy);
    }
}
