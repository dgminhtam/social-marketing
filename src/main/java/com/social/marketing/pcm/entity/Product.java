package com.social.marketing.pcm.entity;

import com.social.marketing.entity.AbstractEntity;
import com.social.marketing.media.entity.Media;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;

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
    private String slug;

    @Column
    private String description;

    @Column
    private BigDecimal price;

    @Column
    private BigDecimal originPrice;

    @OneToOne(fetch = FetchType.LAZY)
    private Media image;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.DRAFT;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
