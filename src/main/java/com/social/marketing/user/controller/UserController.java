package com.social.marketing.user.controller;

import com.social.marketing.user.model.UserResponse;
import com.social.marketing.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping(value = "/profile")
    public UserResponse getProfile() {
        return null;
    }
}
