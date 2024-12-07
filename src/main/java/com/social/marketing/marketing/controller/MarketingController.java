package com.social.marketing.marketing.controller;

import com.social.marketing.marketing.model.response.ServiceResponse;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/marketing")
public class MarketingController {

    @Resource
    private com.social.marketing.marketing.service.MarketingService marketingService;

    @GetMapping
    public List<ServiceResponse> getServices() {
        return marketingService.fetchData();
    }
}
