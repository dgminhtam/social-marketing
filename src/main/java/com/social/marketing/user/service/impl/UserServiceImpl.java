package com.social.marketing.user.service.impl;

import com.social.marketing.exception.NotFoundException;
import com.social.marketing.user.entity.User;
import com.social.marketing.user.repository.UserRepository;
import com.social.marketing.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found."));
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
