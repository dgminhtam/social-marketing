package com.social.marketing.media.entity;

import com.social.marketing.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "medias")
@Getter
@Setter
@FieldNameConstants
public class Media extends AbstractEntity {

    @Column(nullable = false)
    private String altText;

    @Column
    private String mimeType;

    @Column
    private Long fileSizeInByte;

    @Column(nullable = false)
    private String urlOriginal;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> variants;
}