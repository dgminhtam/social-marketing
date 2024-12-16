package com.social.marketing.auth.model.response;

public record LoginResponse(String accessToken, Long expiresIn, String tokenType) {
}
