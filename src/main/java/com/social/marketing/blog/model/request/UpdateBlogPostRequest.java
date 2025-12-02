package com.social.marketing.blog.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBlogPostRequest {
    private String title;
    private String slug;
    private String content;
    private String shortDescription;
    private Long thumbnailId;
    private Boolean isVisible;
}
