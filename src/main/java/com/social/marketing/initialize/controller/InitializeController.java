package com.social.marketing.initialize.controller;

import com.social.marketing.initialize.service.InitializeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/initialize")
public class InitializeController {

    @Resource
    private InitializeService initializeService;

    @GetMapping("/users")
    public void initUsers() {
        initializeService.initUsers();
    }
}
