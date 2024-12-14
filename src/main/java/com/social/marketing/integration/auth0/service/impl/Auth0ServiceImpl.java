package com.social.marketing.integration.auth0.service.impl;

import com.okta.spring.boot.oauth.config.OktaOAuth2Properties;
import com.social.marketing.integration.auth0.model.request.Auth0LoginRequest;
import com.social.marketing.integration.auth0.model.request.Auth0SignupRequest;
import com.social.marketing.integration.auth0.model.response.Auth0LoginResponse;
import com.social.marketing.integration.auth0.model.response.Auth0SignupResponse;
import com.social.marketing.integration.auth0.service.Auth0Service;
import com.social.marketing.rest.service.RestTemplateService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class Auth0ServiceImpl implements Auth0Service {

    @Resource
    private OktaOAuth2Properties properties;

    private static final String SIGN_UP_URL = "dbconnections/signup";

    private static final String LOG_IN_URL = "oauth/token";

    @Resource
    private RestTemplateService restTemplateService;

    @Override
    public Auth0SignupResponse signup(Auth0SignupRequest request) {
        return restTemplateService.postData(properties.getAudience() + SIGN_UP_URL, null, request, Auth0SignupResponse.class);
    }

    @Override
    public Auth0LoginResponse login(Auth0LoginRequest request) {
        return restTemplateService.postData(properties.getAudience() + LOG_IN_URL, null, request, Auth0LoginResponse.class);
    }
}
