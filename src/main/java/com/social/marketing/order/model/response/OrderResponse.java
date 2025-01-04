package com.social.marketing.order.model.response;

import com.social.marketing.order.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private String description;
    private OrderStatus orderStatus;
    private BigDecimal subTotal;
}
