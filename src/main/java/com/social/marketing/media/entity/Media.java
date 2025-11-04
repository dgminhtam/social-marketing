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

@Entity
@Table(name = "medias")
@Getter
@Setter
@FieldNameConstants
public class Media extends AbstractEntity {

    @Column
    private String fileName;

    @Column
    private String altText;

    @Column
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String description;

    @Column
    private String realFileName;

    @Column
    private String mimeType;

    @Column
    private Long fileSizeInByte;

    @Column
    private String path;

    @Column
    private String url;
}
