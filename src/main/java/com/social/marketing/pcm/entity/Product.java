package com.social.marketing.pcm.entity;

import com.social.marketing.entity.AbstractEntity;
import com.social.marketing.media.entity.Media;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
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

    @NotBlank
    @Column(unique = true)
    private String sku;

    @NotBlank
    @Column
    private String name;

    @Column
    private String slug;

    @Column
    private String description;

    @PositiveOrZero
    @Column
    private BigDecimal price;

    @PositiveOrZero
    @Column
    private BigDecimal originPrice;

    @OneToOne(fetch = FetchType.LAZY)
    private Media image;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

}
