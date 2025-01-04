package com.social.marketing.user.model;

import com.social.marketing.user.entity.Gender;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        Gender gender,
        String picture
) {
}
