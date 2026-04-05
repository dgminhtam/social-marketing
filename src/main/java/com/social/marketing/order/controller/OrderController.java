package com.social.marketing.order.controller;

import com.social.marketing.order.entity.Order;
import com.social.marketing.order.model.response.OrderResponse;
import com.social.marketing.order.service.OrderService;
import com.social.marketing.search.anotation.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public Page<OrderResponse> getOrders(@Search Specification<Order> specification, Pageable pageable) {
        return orderService.getOrders(specification, pageable);
    }
}
