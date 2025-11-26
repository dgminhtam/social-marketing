package com.social.marketing.user.service;

import com.social.marketing.user.entity.User;

public interface UserService {

    User getUserByEmail(String email);

    void save(User user);

    void syncUser(String clerkId, String email, String firstName, String lastName, String imageUrl);
}
