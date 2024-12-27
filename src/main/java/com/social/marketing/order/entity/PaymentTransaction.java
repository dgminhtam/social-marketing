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
@Table(name = "payment-transactions")
@FieldNameConstants
public class PaymentTransaction extends AbstractEntity {

    @Column
    private String bin;

    @Column
    private String accountNumber;

    @Column
    private String accountName;

    @Column
    private BigDecimal amount;

    @Column
    private String description;

    @Column
    private String currencyCode;

    @Column
    private String status;

    @Column
    private String checkoutUrl;

    @Column
    private String qrCode;

    @Column
    private Long expiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column
    private String externalId;
}
