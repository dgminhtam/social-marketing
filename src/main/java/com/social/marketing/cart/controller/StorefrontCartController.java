package com.social.marketing.cart.controller;

import com.social.marketing.cart.model.request.PlaceOrderRequest;
import com.social.marketing.cart.model.response.CartResponse;
import com.social.marketing.cart.service.CartService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/storefront/carts")
public class StorefrontCartController {

    @Resource
    private CartService cartService;

    @GetMapping
    public CartResponse getCart(@RequestParam("link") String link) {
        return cartService.getCart(link);
    }


    @PostMapping
    public CartResponse addToCart(@Valid @RequestBody PlaceOrderRequest request) {
        return cartService.addToCart(request);
    }

    @PostMapping("/checkout")
    public CartResponse checkOut(@RequestParam("link") String link) {
        return cartService.checkout(link);
    }

    @PostMapping("/place-order")
    public CartResponse placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        cartService.addToCart(request);
        return cartService.checkout(request.link());
    }

}
