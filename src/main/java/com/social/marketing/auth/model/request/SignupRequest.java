package com.social.marketing.auth.model.request;

import com.social.marketing.user.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupRequest(@NotBlank @Email String email, @NotBlank String password, @NotBlank String firstName,
                            @NotBlank String lastName, Gender gender) {
}
