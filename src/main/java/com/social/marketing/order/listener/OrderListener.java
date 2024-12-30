package com.social.marketing.order.listener;

import com.social.marketing.integration.payos.event.PayOSPaymentEvent;
import com.social.marketing.integration.payos.model.request.PayOSWebhookPayload;
import com.social.marketing.order.entity.Order;
import com.social.marketing.order.entity.OrderStatus;
import com.social.marketing.order.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

    @Resource
    private OrderService orderService;

    @EventListener
    public void paymentEvent(final PayOSPaymentEvent event) {
        PayOSWebhookPayload payload = event.payload();
        if (payload != null) {
            Order order = orderService.getOrderById(payload.getData().getOrderCode());
            if ("00".equals(payload.getData().getCode())) {
                order.setOrderStatus(OrderStatus.PAID);
            }
            orderService.save(order);
        }
    }

}
