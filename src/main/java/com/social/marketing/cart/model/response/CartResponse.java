package com.social.marketing.cart.model.response;

import com.social.marketing.order.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartResponse {
    private Long id;
    private String email;
    private String link;
    private OrderStatus status;
    private BigDecimal subTotal;
    private BigDecimal grandTotal;
    private Integer totalItems;
    private List<CartEntryResponse> entries = new ArrayList<>();
    private ZonedDateTime createDate;
    private ZonedDateTime lastModifiedDate;
}
