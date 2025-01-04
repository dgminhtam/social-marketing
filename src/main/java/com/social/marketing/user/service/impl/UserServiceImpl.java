package com.social.marketing.user.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.integration.auth0.model.response.Auth0UserInfoResponse;
import com.social.marketing.integration.auth0.service.Auth0Service;
import com.social.marketing.user.entity.User;
import com.social.marketing.user.model.UserResponse;
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
    public UserResponse getCurrentUser() {
        Auth0UserInfoResponse response = auth0Service.userInfo();
        User user = getUserByEmail(response.getEmail());
        return convert(user, response);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found."));
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void initUsers() {
        User user = new User();
        user.setEmail("tamduong633@gmail.com");
    }

    private UserResponse convert(User user, Auth0UserInfoResponse response) {
        return new UserResponse(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getGender(), response.getPicture());
    }
}
