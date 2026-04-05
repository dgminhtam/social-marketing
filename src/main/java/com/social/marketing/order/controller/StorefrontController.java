package com.social.marketing.order.controller;

import com.social.marketing.order.entity.Order;
import com.social.marketing.order.model.request.PlaceOrderRequest;
import com.social.marketing.order.model.response.OrderResponse;
import com.social.marketing.order.service.OrderService;
import com.social.marketing.search.anotation.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/orders")
@RequiredArgsConstructor
public class StorefrontController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse placeOrder(@RequestBody PlaceOrderRequest request) {
        return orderService.placeOrder(request);
    }

    @GetMapping("/by-email/{email}")
    public Page<OrderResponse> getOrders(@PathVariable String email, @Search Specification<Order> specification, Pageable pageable) {
        return orderService.getOrdersByEmail(email, specification, pageable);
    }
}
