package com.social.marketing.user.service;

import com.social.marketing.integration.auth0.model.request.Auth0SignupRequest;

public interface UserService {

    void signup(Auth0SignupRequest request);
}
