package com.social.marketing.pcm.model.response;


import com.social.marketing.media.entity.Media;
import com.social.marketing.media.model.response.MediaResponse;
import com.social.marketing.pcm.entity.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String sku;
    private String slug;
    private String name;
    private String description;
    private BigDecimal originPrice;
    private BigDecimal price;
    private ProductStatus status;
    private MediaResponse image;
    private List<CategoryResponse> categories;
    private List<MediaResponse> gallery;
}
