package com.social.marketing.pcm.entity;

import com.social.marketing.entity.AbstractEntity;
import com.social.marketing.media.entity.Media;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "product_collections")
@FieldNameConstants
public class ProductCollection extends AbstractEntity {

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String slug;

    @Column(length = 1000)
    private String description;

    @Column
    private String metaTitle;

    @Column
    private String metaDescription;

    @Column
    private String metaKeywords;

    @Column
    private Boolean isFeatured = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Media image;

    @Column
    private boolean active = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_set_items", joinColumns = @JoinColumn(name = "product_set_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new HashSet<>();
}