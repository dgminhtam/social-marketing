package com.social.marketing.user.service;

import com.social.marketing.user.entity.User;
import com.social.marketing.user.model.UserResponse;

public interface UserService {

    UserResponse getCurrentUser();

    User getUserByEmail(String email);

    User save(User user);
}
