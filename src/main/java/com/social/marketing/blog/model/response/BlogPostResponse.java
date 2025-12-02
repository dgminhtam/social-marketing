package com.social.marketing.blog.model.response;

import com.social.marketing.media.model.response.MediaResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BlogPostResponse {
    private Long id;
    private String title;
    private String slug;
    private String content;
    private String shortDescription;
    private MediaResponse thumbnail;
    private boolean isVisible;
    private LocalDateTime publishedAt;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
