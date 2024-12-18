package com.social.marketing.user.controller;

import com.social.marketing.user.entity.User;
import com.social.marketing.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping(value = "/profile")
    public User getProfile() {
        return userService.getCurrentUser();
    }
}
