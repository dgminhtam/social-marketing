package com.social.marketing.cart.controller;

import com.social.marketing.cart.model.request.AddToCartRequest;
import com.social.marketing.cart.model.request.PlaceOrderRequest;
import com.social.marketing.cart.model.request.UpdateCartEmailRequest;
import com.social.marketing.cart.model.request.UpdateCartEntryRequest;
import com.social.marketing.cart.model.response.CartResponse;
import com.social.marketing.cart.service.impl.CartServiceImpl;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/storefront/carts")
public class StorefrontCartController {

    @Resource
    private CartServiceImpl cartServiceImpl;

    @GetMapping
    public CartResponse getCart(@RequestParam("sid") String sid) {
        return cartServiceImpl.getCart(sid);
    }

    @PostMapping("/items")
    public CartResponse addToCart(@RequestParam("sid") String sid, @Valid @RequestBody AddToCartRequest request) {
        return cartServiceImpl.addSingleItem(sid, request);
    }

    @PostMapping
    public CartResponse addToCart(@Valid @RequestBody PlaceOrderRequest request) {
        return cartServiceImpl.addToCart(request);
    }

    @PutMapping("/entries/{entryId}")
    public CartResponse updateCartEntry(
            @RequestParam("link") String link,
            @PathVariable Long entryId,
            @Valid @RequestBody UpdateCartEntryRequest request) {
        return cartServiceImpl.updateCartEntry(link, entryId, request);
    }

    @DeleteMapping("/entries/{entryId}")
    public CartResponse removeCartEntry(@RequestParam("link") String link, @PathVariable Long entryId) {
        return cartServiceImpl.removeCartEntry(link, entryId);
    }

    @DeleteMapping
    public CartResponse clearCart(@RequestParam("link") String link) {
        return cartServiceImpl.clearCart(link);
    }

    @PutMapping("/email")
    public CartResponse updateEmail(@RequestParam("link") String link,
            @Valid @RequestBody UpdateCartEmailRequest request) {
        return cartServiceImpl.updateEmail(link, request);
    }

    @PostMapping("/checkout")
    public CartResponse checkOut(@RequestParam("link") String link) {
        return cartServiceImpl.checkout(link);
    }

    @PostMapping("/place-order")
    public CartResponse placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        cartServiceImpl.addToCart(request);
        return cartServiceImpl.checkout(request.link());
    }

}
