package com.social.marketing.order.entity;

import com.social.marketing.entity.AbstractEntity;
import com.social.marketing.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "order_entries")
@FieldNameConstants
public class OrderEntry extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    private BigDecimal price;

    @Column
    private Long quantity;

    @Column
    private BigDecimal subTotal;
}
