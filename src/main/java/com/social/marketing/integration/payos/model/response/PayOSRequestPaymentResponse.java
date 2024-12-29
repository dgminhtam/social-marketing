package com.social.marketing.integration.payos.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayOSRequestPaymentResponse {
    private String code;
    private String desc;
    private DataPayload data;
    private String signature;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataPayload {
        private String bin;
        private String accountNumber;
        private String accountName;
        private int amount;
        private String description;
        private int orderCode;
        private String currency;
        private String paymentLinkId;
        private String status;
        private String checkoutUrl;
        private String qrCode;
        private Long expiredAt;
    }
}
