package com.social.marketing.cart.entity;

import com.social.marketing.entity.AbstractEntity;
import com.social.marketing.user.entity.User;
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

    @OneToOne
    private User user;

    @Column
    private BigDecimal subTotal;

    @Column
    private BigDecimal grandTotal;

    @OneToMany(mappedBy = CartEntry.Fields.cart, cascade = { CascadeType.ALL }, orphanRemoval = true)
    private List<CartEntry> entries = new ArrayList<>();

    @Column
    private String sid;
}