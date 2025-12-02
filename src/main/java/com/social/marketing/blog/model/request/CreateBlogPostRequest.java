package com.social.marketing.blog.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBlogPostRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String slug;

    private String content;

    private String shortDescription;

    private Long thumbnailId;

    private boolean isVisible;
}
