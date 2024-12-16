package com.social.marketing.auth.service.impl;

import com.social.marketing.auth.model.request.LoginRequest;
import com.social.marketing.auth.model.request.SignupRequest;
import com.social.marketing.auth.model.response.LoginResponse;
import com.social.marketing.auth.service.AuthService;
import com.social.marketing.integration.auth0.model.request.Auth0LoginRequest;
import com.social.marketing.integration.auth0.model.request.Auth0SignupRequest;
import com.social.marketing.integration.auth0.model.response.Auth0LoginResponse;
import com.social.marketing.integration.auth0.model.response.Auth0SignupResponse;
import com.social.marketing.integration.auth0.service.Auth0Service;
import com.social.marketing.user.entity.User;
import com.social.marketing.user.repository.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private Auth0Service auth0Service;

    @Resource
    private UserRepository userRepository;

    @Override
    public void signup(SignupRequest request) {
        Auth0SignupRequest auth0SignupRequest = new Auth0SignupRequest();
        auth0SignupRequest.setEmail(request.email());
        auth0SignupRequest.setPassword(request.password());
        Auth0SignupResponse auth0SignupResponse = auth0Service.signup(auth0SignupRequest);
        User user = new User();
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setExternalId(auth0SignupResponse.getId());
        userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Auth0LoginRequest auth0LoginRequest = new Auth0LoginRequest();
        auth0LoginRequest.setUsername(request.email());
        auth0LoginRequest.setPassword(request.password());
        Auth0LoginResponse auth0LoginResponse = auth0Service.login(auth0LoginRequest);
        return new LoginResponse(auth0LoginResponse.getAccessToken(), auth0LoginResponse.getExpiresIn(), auth0LoginResponse.getTokenType());
    }
}
