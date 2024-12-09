package com.social.marketing.auth.service;

import com.social.marketing.auth.model.request.LoginRequest;
import com.social.marketing.auth.model.request.SignupRequest;
import com.social.marketing.auth.model.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {

    AuthResponse signup(SignupRequest request);

    AuthResponse token(LoginRequest request);

    void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;
}
