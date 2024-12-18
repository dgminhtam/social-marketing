package com.social.marketing.integration.auth0.service.impl;

import com.okta.spring.boot.oauth.config.OktaOAuth2Properties;
import com.social.marketing.integration.auth0.model.request.Auth0LoginRequest;
import com.social.marketing.integration.auth0.model.request.Auth0SignupRequest;
import com.social.marketing.integration.auth0.model.response.Auth0LoginResponse;
import com.social.marketing.integration.auth0.model.response.Auth0SignupResponse;
import com.social.marketing.integration.auth0.model.response.Auth0UserInfoResponse;
import com.social.marketing.integration.auth0.service.Auth0Service;
import com.social.marketing.rest.service.RestTemplateService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class Auth0ServiceImpl implements Auth0Service {

    private static final String SIGN_UP_URL = "dbconnections/signup";
    private static final String LOG_IN_URL = "oauth/token";
    private static final String USER_INFO_URL = "userinfo";

    @Resource
    private OktaOAuth2Properties properties;
    @Resource
    private RestTemplateService restTemplateService;
    @Resource
    private HttpServletRequest httpServletRequest;

    @Override
    public Auth0SignupResponse signup(Auth0SignupRequest request) {
        return restTemplateService.postData(properties.getIssuer() + SIGN_UP_URL, null, request, Auth0SignupResponse.class);
    }

    @Override
    public Auth0LoginResponse login(Auth0LoginRequest request) {
        return restTemplateService.postData(properties.getIssuer() + LOG_IN_URL, null, request, Auth0LoginResponse.class);
    }

    @Override
    public Auth0UserInfoResponse userInfo() {
        HttpHeaders headers = new HttpHeaders();
        String token = extractTokenFromRequest();
        if (token == null) {
            throw new RuntimeException("Authorization token is missing");
        }
        headers.set("Authorization", "Bearer " + token);
        return restTemplateService.postData(properties.getIssuer() + USER_INFO_URL, headers, null, Auth0UserInfoResponse.class);
    }

    private String extractTokenFromRequest() {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
