package com.social.marketing.user.entity;

import com.social.marketing.entity.AbstractEntity;
import com.social.marketing.media.entity.Media;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends AbstractEntity {

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @OneToOne(fetch = FetchType.LAZY)
    private Media image;

    @Column(unique = true, nullable = false)
    private String externalId;

}
