package com.social.marketing.order.model.response;

import com.social.marketing.order.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private String email;
    private String link;
    private OrderStatus status;
    private BigDecimal subTotal;
    private ZonedDateTime createDate;
    private ZonedDateTime lastModifiedDate;
}
