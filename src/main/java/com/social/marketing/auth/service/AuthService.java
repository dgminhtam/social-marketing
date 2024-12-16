package com.social.marketing.auth.service;

import com.social.marketing.auth.model.request.LoginRequest;
import com.social.marketing.auth.model.request.SignupRequest;
import com.social.marketing.auth.model.response.LoginResponse;

public interface AuthService {

    void signup(SignupRequest request);

    LoginResponse login(LoginRequest request);
}
