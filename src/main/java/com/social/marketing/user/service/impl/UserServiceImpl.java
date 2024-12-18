package com.social.marketing.user.service.impl;

import com.social.marketing.integration.auth0.model.response.Auth0UserInfoResponse;
import com.social.marketing.integration.auth0.service.Auth0Service;
import com.social.marketing.user.entity.User;
import com.social.marketing.user.repository.UserRepository;
import com.social.marketing.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private Auth0Service auth0Service;

    @Resource
    private UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        Auth0UserInfoResponse response = auth0Service.userInfo();
        String email = response.getEmail();
        return userRepository.findByEmail(email);
    }
}
