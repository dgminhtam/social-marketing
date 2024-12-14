package com.social.marketing.rest.service.impl;

import com.social.marketing.rest.service.RestTemplateService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateServiceImpl implements RestTemplateService {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateServiceImpl.class);

    @Resource
    private RestTemplate restTemplate;

    @Override
    public <T> T getData(String url, HttpHeaders headers, Class<T> responseType) {
        return makeRequest(url, HttpMethod.GET, headers, null, responseType);
    }

    @Override
    public <T> T postData(String url, HttpHeaders headers, Object body, Class<T> responseType) {
        return makeRequest(url, HttpMethod.POST, headers, body, responseType);
    }

    @Override
    public <T> T putData(String url, HttpHeaders headers, Object body, Class<T> responseType) {
        return makeRequest(url, HttpMethod.PUT, headers, body, responseType);
    }

    @Override
    public void deleteData(String url, HttpHeaders headers) {
        makeRequest(url, HttpMethod.DELETE, headers, null, Void.class);
    }

    @Override
    public <T> T patchData(String url, HttpHeaders headers, Object body, Class<T> responseType) {
        return makeRequest(url, HttpMethod.PATCH, headers, body, responseType);
    }

    private <T> T makeRequest(String url, HttpMethod method, HttpHeaders headers, Object body, Class<T> responseType) {
        try {
            HttpEntity<Object> entity = new HttpEntity<>(body, prepareHeaders(headers));
            ResponseEntity<T> responseEntity = restTemplate.exchange(url, method, entity, responseType);
            return responseEntity.getBody();
        } catch (HttpStatusCodeException ex) {
            logger.error("Error during HTTP request: {} {}", method, url, ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error during HTTP request: {} {}", method, url, ex);
            throw new RuntimeException("An unexpected error occurred while making the HTTP request", ex);
        }
    }

    private HttpHeaders prepareHeaders(HttpHeaders customHeaders) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (customHeaders != null) {
            headers.addAll(customHeaders);
        }
        return headers;
    }
}
