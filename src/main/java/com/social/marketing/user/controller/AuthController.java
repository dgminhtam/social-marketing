package com.social.marketing.user.controller;

import com.social.marketing.integration.auth0.model.request.Auth0SignupRequest;
import com.social.marketing.user.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private UserService userService;

    @PostMapping("/signup")
    public void signup(@Valid Auth0SignupRequest request) {
        userService.signup(request);
    }
}
