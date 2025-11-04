package com.social.marketing.cart.controller;

import com.social.marketing.cart.model.response.CartResponse;
import com.social.marketing.order.model.request.PlaceOrderRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/storefront/carts")
public class StorefrontCartController {

    @GetMapping
    public CartResponse getCart() {
        return null;
    }


    @PostMapping
    public CartResponse addToCart(@RequestBody PlaceOrderRequest request) {
        return null;
    }

    @PostMapping("/checkout")
    public CartResponse checkOut() {
        return null;
    }

    @PostMapping("/place-order")
    public CartResponse placeOrder() {
        return null;
    }

}
