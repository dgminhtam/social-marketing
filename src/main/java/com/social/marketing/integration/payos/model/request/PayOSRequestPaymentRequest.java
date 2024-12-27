package com.social.marketing.integration.payos.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PayOSRequestPaymentRequest extends AbstractPayOSRequest {
    private Long orderCode;
    private int amount;
    private String description;
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;
    private String buyerAddress;
    private List<Item> items;
    private String cancelUrl;
    private String returnUrl;
    private Long expiredAt;

    @Data
    public static class Item {
        private String name;
        private Long quantity;
        private BigDecimal price;
    }
}
