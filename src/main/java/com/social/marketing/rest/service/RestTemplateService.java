package com.social.marketing.rest.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;

public interface RestTemplateService {
    
    <T> T getData(String url, HttpHeaders headers, ParameterizedTypeReference<T> responseType);

    <T> T postData(String url, HttpHeaders headers, Object body, ParameterizedTypeReference<T> responseType);

    <T> T putData(String url, HttpHeaders headers, Object body, ParameterizedTypeReference<T> responseType);

    void deleteData(String url, HttpHeaders headers);

    <T> T patchData(String url, HttpHeaders headers, Object body, ParameterizedTypeReference<T> responseType);
}
