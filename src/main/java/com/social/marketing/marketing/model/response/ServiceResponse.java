package com.social.marketing.marketing.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceResponse {

    @JsonProperty("service")
    private String service;

    @JsonProperty("name")
    private String name;

    @JsonProperty("category")
    private String category;

    @JsonProperty("rate")
    private String rate;

    @JsonProperty("min")
    private String min;

    @JsonProperty("max")
    private String max;

    @JsonProperty("type")
    private String type;

    @JsonProperty("desc")
    private String desc;
}