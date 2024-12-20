package com.social.marketing.integration.auth0.aspect;

import com.okta.spring.boot.oauth.config.OktaOAuth2Properties;
import com.social.marketing.integration.auth0.model.request.Auth0LoginRequest;
import com.social.marketing.integration.auth0.model.request.Auth0SignupRequest;
import jakarta.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Auth0Aspect {

    @Resource
    private OktaOAuth2Properties properties;

    @Before("execution(* com.social.marketing.integration.auth0.service.impl.Auth0ServiceImpl.login(..)) && args(request,..)")
    public void addClientInfoToSignUpRequest(JoinPoint joinPoint, Auth0LoginRequest request) {
        if (request != null) {
            if (request.getGrantType() == null) {
                request.setGrantType("password");
            }
            if (request.getClientId() == null) {
                request.setClientId(properties.getClientId());
            }
            if (request.getClientSecret() == null) {
                request.setClientSecret(properties.getClientSecret());
            }
            if (request.getScope() == null) {
                request.setScope("openid profile email");
            }
            if (request.getAudience() == null) {
                request.setAudience(properties.getAudience());
            }
        }
    }

    @Before("execution(* com.social.marketing.integration.auth0.service.impl.Auth0ServiceImpl.signup(..)) && args(request,..)")
    public void addClientInfoToSignUpRequest(JoinPoint joinPoint, Auth0SignupRequest request) {
        if (request != null) {
            if (request.getClientId() == null) {
                request.setClientId(properties.getClientId());
            }
            if (request.getConnection() == null) {
                request.setConnection("Username-Password-Authentication");
            }
        }
    }
}
