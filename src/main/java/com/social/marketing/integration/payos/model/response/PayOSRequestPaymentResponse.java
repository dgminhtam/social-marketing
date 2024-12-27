package com.social.marketing.integration.payos.model.response;

import lombok.Data;

@Data
public class PayOSRequestPaymentResponse {
    private String code;
    private String desc;
    private DataPayload data;
    private String signature;

    @Data
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
    }
}
