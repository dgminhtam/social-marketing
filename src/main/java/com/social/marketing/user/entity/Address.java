package com.social.marketing.user.entity;

import com.social.marketing.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@Entity
@Table(name = "addresses")
@FieldNameConstants
public class Address extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    @Column(nullable = false)
    private String contactName;

    @NotBlank
    @Column(nullable = false)
    private String phone;

    @NotBlank
    @Column(nullable = false)
    private String addressLine1;

    @Column
    private String addressLine2;

    @NotBlank
    @Column(nullable = false)
    private String city;

    @Column
    private String state;

    @Column
    private String zipCode;

    @NotBlank
    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private Boolean isDefault = false;
}
