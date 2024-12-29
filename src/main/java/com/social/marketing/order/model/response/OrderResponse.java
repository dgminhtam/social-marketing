package com.social.marketing.order.model.response;

import com.social.marketing.order.entity.OrderStatus;
import com.social.marketing.product.model.response.ProductResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderResponse {
    private String code;
    private String description;
    private OrderStatus orderStatus;
    private BigDecimal subTotal;
}
