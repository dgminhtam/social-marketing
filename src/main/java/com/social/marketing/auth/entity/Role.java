package com.social.marketing.auth.entity;

import com.social.marketing.product.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "roles")
public class Role extends AbstractEntity {

    @Column(length = 20)
    private String name;
}
