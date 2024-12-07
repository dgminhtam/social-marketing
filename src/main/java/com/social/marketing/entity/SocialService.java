package com.social.marketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SocialService extends AbstractEntity {

    @Column
    private String code;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private ServiceType type;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
