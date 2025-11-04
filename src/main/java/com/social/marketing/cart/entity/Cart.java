package com.social.marketing.cart.entity;

import com.social.marketing.entity.AbstractEntity;
import com.social.marketing.order.entity.OrderEntry;
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
@Table(name = "carts")
@FieldNameConstants
public class Cart extends AbstractEntity {

    @Column
    private String email;

    @Column
    private String description;

    @Column
    private BigDecimal subTotal;

    @Column
    private BigDecimal grandTotal;

    @OneToMany(mappedBy = OrderEntry.Fields.order, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<CartEntry> entries = new ArrayList<>();
}