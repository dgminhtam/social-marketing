package com.social.marketing.user.service;

import com.social.marketing.user.entity.User;

public interface UserService {

    User getUserByEmail(String email);

    void save(User user);
}
