package com.social.marketing.product.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryResponse {

    private Long id;

    private String name;

    private String description;

    private List<CategoryResponse> children;
}
