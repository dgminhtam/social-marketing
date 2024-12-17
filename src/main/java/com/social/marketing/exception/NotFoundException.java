package com.social.marketing.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends BaseException {

    public NotFoundException(final String message, final Object... args) {
        super(HttpStatus.NOT_FOUND, message, args);
    }
}
