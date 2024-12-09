package com.social.marketing.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends AbstractEntity {

    @Column(unique = true)
    private String sku;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
