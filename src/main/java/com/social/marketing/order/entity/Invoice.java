package com.social.marketing.order.entity;

import com.social.marketing.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@Entity
@Table(name = "invoices",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code")
        }
)
@FieldNameConstants
public class Invoice extends AbstractEntity {

    @Column
    private String code;
}
