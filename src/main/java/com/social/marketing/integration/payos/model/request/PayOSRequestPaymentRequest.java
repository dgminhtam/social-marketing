package com.social.marketing.integration.payos.model.request;

import lombok.Data;

@Data
public class PayOSRequestPaymentRequest {
    
    private Long orderCode;
    private int amount;
    private String description;
    private Long expiredAt;
}
