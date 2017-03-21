package com.opopov.cloud.image.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by elx01 on 3/21/17.
 */

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidImageConfigException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidImageConfigException(String message, Exception causedBy) {
        super(message, causedBy);
    }

    public InvalidImageConfigException(String message) {
        super(message);
    }
}
