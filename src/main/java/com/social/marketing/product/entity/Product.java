package com.social.marketing.product.entity;

import com.social.marketing.entity.AbstractEntity;
import com.social.marketing.media.entity.Media;
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
@Table(name = "products")
@FieldNameConstants
public class Product extends AbstractEntity {

    @Column(unique = true)
    private String sku;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private BigDecimal price;

    @Column
    private BigDecimal originPrice;

    @Column
    private Long maxOrderQuantity;

    @Column
    private Long minOrderQuantity;

    @Column
    private Boolean isBase = false;

    @OneToOne(fetch = FetchType.LAZY)
    private Media image;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.DRAFT;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "base_id")
    private Product base;

    @OneToMany(mappedBy = Fields.base, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Product> variants = new ArrayList<>();

    @Column
    private String externalId;
}
