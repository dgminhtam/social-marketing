package com.social.marketing.user.service.impl;

import com.social.marketing.integration.auth0.model.request.Auth0SignupRequest;
import com.social.marketing.user.service.UserService;
import com.social.marketing.rest.service.RestTemplateService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private RestTemplateService restTemplateService;

    @Override
    public void signup(Auth0SignupRequest request) {
    }
}
