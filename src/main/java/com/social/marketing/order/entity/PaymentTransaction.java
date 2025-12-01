package com.social.marketing.order.entity;

import com.social.marketing.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "payment_transactions")
@FieldNameConstants
public class PaymentTransaction extends AbstractEntity {

    @Column
    private BigDecimal amount;

    @Column
    private String description;

    @Column
    private String currencyCode;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentProvider provider;

    @Column
    private String checkoutUrl;

    @Column
    private Long expiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column
    private String externalId;
}
