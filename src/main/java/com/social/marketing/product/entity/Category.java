package com.social.marketing.product.entity;

import com.social.marketing.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@FieldNameConstants
@EqualsAndHashCode(callSuper = true)
@Table(name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        }
)
public class Category extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = Fields.parent, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();
}
