package com.social.marketing.integration.payos.model.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayOSRequestPaymentResponse {
    private String bin;
    private String accountNumber;
    private String accountName;
    private BigDecimal amount;
    private String description;
    private Long orderCode;
    private String currency;
    private String paymentLinkId;
    private String status;
    private String checkoutUrl;
    private String qrCode;
    private Long expiredAt;
}
