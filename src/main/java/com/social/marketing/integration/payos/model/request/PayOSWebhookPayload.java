package com.social.marketing.integration.payos.model.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayOSWebhookPayload {
    private String code;
    private String desc;
    private boolean success;
    private DataPayload data;
    private String signature;

    @Data
    public static class DataPayload {
        private Long orderCode;
        private BigDecimal amount;
        private String description;
        private String accountNumber;
        private String reference;
        private String transactionDateTime;
        private String currency;
        private String paymentLinkId;
        private String code;
        private String desc;
        private String counterAccountBankId;
        private String counterAccountBankName;
        private String counterAccountName;
        private String counterAccountNumber;
        private String virtualAccountName;
        private String virtualAccountNumber;
    }
}
