package com.social.marketing.integration.auth0.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Auth0SignupResponse {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("email_verified")
    private boolean emailVerified;
}
