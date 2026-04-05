package com.social.marketing.user.controller;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.user.model.UserResponse;
import com.social.marketing.user.service.UserService;
import com.social.marketing.user.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/storefront/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/profile")
    public UserResponse getProfile() {
        User user = userService.getCurrentUser();
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getImage() != null ? user.getImage().getUrlOriginal() : null);
    }
}
