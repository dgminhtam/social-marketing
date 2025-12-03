package com.social.marketing.cart;

import com.social.marketing.cart.repository.CartRepository;
import com.social.marketing.cart.service.CartService;
import com.social.marketing.cart.service.impl.CartServiceImpl;
import com.social.marketing.order.service.OrderService;
import com.social.marketing.pcm.service.StorefrontProductService;
import com.social.marketing.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CartConfiguration {

    @Bean
    public CartService cartService(CartRepository cartRepository, StorefrontProductService storefrontProductService, OrderService orderService, UserService userService) {
        return new CartServiceImpl(cartRepository, storefrontProductService, orderService, userService);
    }

}
