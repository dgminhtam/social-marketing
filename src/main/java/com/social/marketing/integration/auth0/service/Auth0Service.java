package com.social.marketing.integration.auth0.service;

import com.social.marketing.integration.auth0.model.request.Auth0LoginRequest;
import com.social.marketing.integration.auth0.model.request.Auth0SignupRequest;
import com.social.marketing.integration.auth0.model.response.Auth0LoginResponse;
import com.social.marketing.integration.auth0.model.response.Auth0SignupResponse;

public interface Auth0Service {

    Auth0SignupResponse signup(Auth0SignupRequest request);

    Auth0LoginResponse login(Auth0LoginRequest request);
}
