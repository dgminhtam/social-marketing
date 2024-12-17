package com.social.marketing.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {

    private final HttpStatus status;
    private final transient Object[] args;

    public BaseException(final HttpStatus status, final String message, final Object... args) {
        super(message);
        this.status = status;
        this.args = args;
    }
}