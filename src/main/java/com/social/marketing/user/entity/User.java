package com.social.marketing.user.entity;

import com.social.marketing.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    private String first_name;

    @Column
    private String last_name;

    @Column
    private String _id;

}
