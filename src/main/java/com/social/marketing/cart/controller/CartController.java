package com.social.marketing.cart.controller;

import com.social.marketing.order.entity.Order;
import com.social.marketing.order.model.response.OrderResponse;
import com.social.marketing.order.service.OrderService;
import com.social.marketing.search.anotation.Search;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class CartController {

    @Resource
    private OrderService orderService;

    @GetMapping
    public Page<OrderResponse> getOrders(@Search Specification<Order> specification, Pageable pageable) {
        return orderService.getOrders(specification, pageable);
    }
}
