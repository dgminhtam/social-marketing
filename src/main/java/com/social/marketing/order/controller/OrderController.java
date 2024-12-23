package com.social.marketing.order.controller;

import com.social.marketing.order.entity.Order;
import com.social.marketing.order.model.request.PlaceOrderRequest;
import com.social.marketing.order.model.response.OrderResponse;
import com.social.marketing.order.service.OrderService;
import com.social.marketing.search.anotation.Search;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("/place-order")
    public OrderResponse placeOrder(@RequestBody PlaceOrderRequest request) {
        return orderService.placeOrder(request);
    }

    @GetMapping
    public Page<OrderResponse> getOrders(@Search Specification<Order> specification, Pageable pageable) {
        return orderService.getOrders(specification, pageable);
    }
}
