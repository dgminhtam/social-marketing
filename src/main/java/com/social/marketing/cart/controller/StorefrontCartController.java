package com.social.marketing.cart.controller;

import com.social.marketing.cart.model.request.AddToCartRequest;
import com.social.marketing.cart.model.request.PlaceOrderRequest;
import com.social.marketing.cart.model.request.UpdateCartEmailRequest;
import com.social.marketing.cart.model.request.UpdateCartEntryRequest;
import com.social.marketing.cart.model.response.CartResponse;
import com.social.marketing.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/storefront/carts")
@RequiredArgsConstructor
public class StorefrontCartController {

    private final CartService cartService;

    @GetMapping
    public CartResponse getCart(@RequestParam("sid") String sid) {
        return cartService.getCart(sid);
    }

    @PostMapping("/items")
    public CartResponse addToCart(@RequestParam("sid") String sid, @Valid @RequestBody AddToCartRequest request) {
        return cartService.addSingleItem(sid, request);
    }

    @PostMapping
    public CartResponse addToCart(@Valid @RequestBody PlaceOrderRequest request) {
        return cartService.addToCart(request);
    }

    @PutMapping("/entries/{entryId}")
    public CartResponse updateCartEntry(
            @RequestParam("link") String link,
            @PathVariable Long entryId,
            @Valid @RequestBody UpdateCartEntryRequest request) {
        return cartService.updateCartEntry(link, entryId, request);
    }

    @DeleteMapping("/entries/{entryId}")
    public CartResponse removeCartEntry(@RequestParam("link") String link, @PathVariable Long entryId) {
        return cartService.removeCartEntry(link, entryId);
    }

    @DeleteMapping
    public CartResponse clearCart(@RequestParam("link") String link) {
        return cartService.clearCart(link);
    }

    @PutMapping("/email")
    public CartResponse updateEmail(@RequestParam("link") String link,
            @Valid @RequestBody UpdateCartEmailRequest request) {
        return cartService.updateEmail(link, request);
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
