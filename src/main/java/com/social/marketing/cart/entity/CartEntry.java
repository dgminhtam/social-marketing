package com.social.marketing.cart.entity;

import com.social.marketing.entity.AbstractEntity;
import com.social.marketing.pcm.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "cart_entries")
@FieldNameConstants
public class CartEntry extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

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
