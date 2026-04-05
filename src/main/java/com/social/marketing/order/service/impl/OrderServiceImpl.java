package com.social.marketing.order.service.impl;

import com.social.marketing.exception.BadRequestException;
import com.social.marketing.exception.NotFoundException;
import com.social.marketing.order.entity.Order;
import com.social.marketing.order.entity.OrderEntry;
import com.social.marketing.order.entity.OrderStatus;
import com.social.marketing.order.model.request.OrderEntryRequest;
import com.social.marketing.order.model.request.PlaceOrderRequest;
import com.social.marketing.order.model.response.OrderResponse;
import com.social.marketing.order.repository.OrderRepository;
import com.social.marketing.order.service.OrderService;
import com.social.marketing.pcm.entity.Product;
import com.social.marketing.pcm.service.StorefrontProductService;
import jakarta.transaction.Transactional;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final StorefrontProductService storefrontProductService;

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        Order order = buildOrder(request);
        orderRepository.save(order);
        return convert(order);
    }

    private Order buildOrder(PlaceOrderRequest request) {
        Order order = new Order();
        order.setEmail(request.email());
        order.setDescription(request.description());
        order.setStatus(OrderStatus.OPEN);
        List<OrderEntry> entries = buildOrderEntries(request.entries(), order);
        order.setEntries(entries);
        order.setSubTotal(entries.stream()
                .map(OrderEntry::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        return order;
    }

    private List<OrderEntry> buildOrderEntries(List<OrderEntryRequest> entriesRequest, Order order) {
        List<OrderEntry> entries = new ArrayList<>();
        if (CollectionUtils.isEmpty(entriesRequest)) {
            throw new BadRequestException("No product selected.");
        }
        entriesRequest.forEach(entryRequest -> {
            OrderEntry orderEntry = new OrderEntry();
            Product product = storefrontProductService.getBySku(entryRequest.sku());
            BigDecimal price = product.getPrice();
            if (Objects.isNull(price)) {
                throw new BadRequestException("Product price cannot be null.");
            }
            orderEntry.setProduct(product);
            orderEntry.setPrice(price);
            orderEntry.setQuantity(entryRequest.quantity());
            orderEntry.setName(product.getName());
            orderEntry.setDescription(entryRequest.description());
            orderEntry.setSubTotal(price.multiply(BigDecimal.valueOf(entryRequest.quantity())));
            orderEntry.setOrder(order);
            entries.add(orderEntry);
        });
        return entries;
    }

    @Override
    public OrderResponse convert(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setEmail(order.getEmail());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setSubTotal(order.getSubTotal());
        orderResponse.setCreateDate(order.getCreatedDate());
        orderResponse.setLastModifiedDate(order.getLastModifiedDate());
        return orderResponse;
    }

    @Override
    public Page<OrderResponse> getOrders(Specification<Order> specification, Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(specification, pageable);
        List<OrderResponse> orderResponses = orders.getContent().stream().map(this::convert).toList();
        return new PageImpl<>(orderResponses, orders.getPageable(), orders.getTotalElements());
    }

    @Override
    public Order getOrderById(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            throw new NotFoundException("Order not found.");
        }
        return orderOpt.get();
    }

    @Override
    public Page<OrderResponse> getOrdersByEmail(String email, Specification<Order> specification, Pageable pageable) {
        Specification<Order> spec =
                (root, query, builder) -> builder.equal(root.get(Order.Fields.email), email);
        Page<Order> orders = orderRepository.findAll(spec.and(specification), pageable);
        List<OrderResponse> orderResponses = orders.getContent().stream().map(this::convert).toList();
        return new PageImpl<>(orderResponses, orders.getPageable(), orders.getTotalElements());
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }
}
