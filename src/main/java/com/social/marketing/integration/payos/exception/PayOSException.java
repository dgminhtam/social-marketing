package com.social.marketing.integration.payos.exception;

import com.social.marketing.exception.BaseException;
import org.springframework.http.HttpStatus;

public class PayOSException extends BaseException {

    public PayOSException(String message, Object... args) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, args);
    }
}
