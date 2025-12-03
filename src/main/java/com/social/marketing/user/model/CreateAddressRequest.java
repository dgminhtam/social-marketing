package com.social.marketing.user.model;

import jakarta.validation.constraints.NotBlank;

public record CreateAddressRequest(
        @NotBlank String contactName,
        @NotBlank String phone,
        @NotBlank String addressLine1,
        String addressLine2,
        @NotBlank String city,
        String state,
        String zipCode,
        @NotBlank String country,
        Boolean isDefault) {
}
