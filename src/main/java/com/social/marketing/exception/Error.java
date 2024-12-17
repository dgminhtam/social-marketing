package com.social.marketing.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Error {

    private LocalDateTime timestamp;
    private String message;
}


