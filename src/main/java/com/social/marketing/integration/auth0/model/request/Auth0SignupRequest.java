package com.social.marketing.integration.auth0.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Auth0SignupRequest {

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("connection")
    private String connection;
}
