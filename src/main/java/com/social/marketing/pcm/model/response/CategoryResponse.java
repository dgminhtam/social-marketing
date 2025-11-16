package com.social.marketing.pcm.model.response;

import com.social.marketing.media.model.response.MediaResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryResponse {

    private Long id;

    private String name;

    private String slug;

    private String description;

    private boolean active;

    private MediaResponse image;

    private Long parentId;

    private List<CategoryResponse> children;

    private String createdDate;

    private String lastModifiedDate;
}
