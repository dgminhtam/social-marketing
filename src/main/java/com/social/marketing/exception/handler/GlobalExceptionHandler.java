package com.social.marketing.exception.handler;

import com.social.marketing.exception.BaseException;
import com.social.marketing.exception.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    private ResponseEntity<Object> handleCommonException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Error.builder().timestamp(LocalDateTime.now()).message("Internal server error.").build());
    }

    @ExceptionHandler(value = {BaseException.class})
    private ResponseEntity<Object> handleCommonException(final BaseException e) {
        return ResponseEntity.status(e.getStatus())
                .body(Error.builder().timestamp(LocalDateTime.now()).message(e.getMessage()).build());
    }
}
