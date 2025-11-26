package com.social.marketing.user.model;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        String picture
) {
}
