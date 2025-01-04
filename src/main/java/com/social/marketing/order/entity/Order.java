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
@Table(name = "orders")
@FieldNameConstants
public class Order extends AbstractEntity {

    @Column
    private String email;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    private Long quantity;

    @Column
    private BigDecimal subTotal;

    @OneToMany(mappedBy = PaymentTransaction.Fields.order, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<PaymentTransaction> paymentTransactions = new ArrayList<>();

    @Column
    private String externalId;
}
