package com.social.marketing.auth.controller;

import com.social.marketing.auth.model.request.LoginRequest;
import com.social.marketing.auth.model.request.SignupRequest;
import com.social.marketing.auth.model.response.AuthResponse;
import com.social.marketing.auth.service.AuthService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/token")
    public ResponseEntity<AuthResponse> token(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.token(request));
    }
}
