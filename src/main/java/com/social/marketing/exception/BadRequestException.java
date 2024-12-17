package com.social.marketing.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends BaseException {

    public BadRequestException(final String message, final Object... args) {
        super(HttpStatus.BAD_REQUEST, message, args);
    }
}
