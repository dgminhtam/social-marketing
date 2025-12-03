package com.social.marketing.user.model;

public record AddressResponse(
        Long id,
        String contactName,
        String phone,
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String zipCode,
        String country,
        Boolean isDefault) {
}
