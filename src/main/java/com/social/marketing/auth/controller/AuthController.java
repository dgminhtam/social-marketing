package com.social.marketing.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class AuthController {

    @GetMapping("/health")
    public String healthCheck() {
        return TimeUnit;
    }

}
