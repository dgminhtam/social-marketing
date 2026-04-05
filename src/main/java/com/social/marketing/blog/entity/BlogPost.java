package com.social.marketing.blog.entity;

import com.social.marketing.entity.AbstractEntity;
import com.social.marketing.media.entity.Media;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "blog_posts")
@FieldNameConstants
public class BlogPost extends AbstractEntity {

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 500)
    private String shortDescription;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_media_id")
    private Media thumbnail;

    @Column(nullable = false)
    private boolean isVisible = false;

    @Column
    private LocalDateTime publishedAt;
}
