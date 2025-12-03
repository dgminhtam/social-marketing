package com.social.marketing.user;

import com.social.marketing.media.service.MediaService;
import com.social.marketing.user.repository.UserRepository;
import com.social.marketing.user.service.UserService;
import com.social.marketing.user.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UserConfiguration {

    @Bean
    public UserService userService(UserRepository userRepository, MediaService mediaService) {
        return new UserServiceImpl(userRepository, mediaService);
    }

    @Bean
    public com.social.marketing.user.service.AddressService addressService(
            com.social.marketing.user.repository.AddressRepository addressRepository,
            UserService userService) {
        return new com.social.marketing.user.service.impl.AddressServiceImpl(addressRepository, userService);
    }

}
