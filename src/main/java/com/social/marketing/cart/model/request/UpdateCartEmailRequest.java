package com.social.marketing.cart.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateCartEmailRequest(
        @NotBlank(message = "Email không được để trống") @Email(message = "Email không hợp lệ") String email) {
}
