package com.social.marketing.cart.service;

import com.social.marketing.cart.entity.Cart;
import com.social.marketing.cart.model.request.PlaceOrderRequest;
import com.social.marketing.cart.model.request.UpdateCartEmailRequest;
import com.social.marketing.cart.model.response.CartResponse;
import jakarta.transaction.Transactional;

public interface CartService {

    Cart getBySid(String sid);

    @Transactional
    CartResponse addToCart(PlaceOrderRequest request);

    @Transactional
    CartResponse checkout(String sid);

    @Transactional
    CartResponse addSingleItem(String link, com.social.marketing.cart.model.request.AddToCartRequest request);

    @Transactional
    CartResponse updateCartEntry(String link, Long entryId,
                                 com.social.marketing.cart.model.request.UpdateCartEntryRequest request);

    @Transactional
    CartResponse removeCartEntry(String link, Long entryId);

    @Transactional
    CartResponse clearCart(String sid);

    @Transactional
    CartResponse updateEmail(String sid, UpdateCartEmailRequest request);

    CartResponse getCart(String sid);
}
