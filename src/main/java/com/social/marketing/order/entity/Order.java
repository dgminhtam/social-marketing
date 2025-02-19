package com.social.marketing.order.entity;

import com.social.marketing.entity.AbstractEntity;
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

    @Column
    private String link;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @Column
    private String description;

    @Column
    private BigDecimal subTotal;

    @OneToMany(mappedBy = OrderEntry.Fields.order, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<OrderEntry> entries = new ArrayList<>();

    @OneToMany(mappedBy = PaymentTransaction.Fields.order, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<PaymentTransaction> paymentTransactions = new ArrayList<>();

    @Column
    private String externalId;
}
