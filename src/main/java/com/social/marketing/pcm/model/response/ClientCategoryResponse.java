package com.social.marketing.pcm.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClientCategoryResponse {

    private Long id;

    private String name;

    private String description;

    private List<ClientCategoryResponse> children;
}
