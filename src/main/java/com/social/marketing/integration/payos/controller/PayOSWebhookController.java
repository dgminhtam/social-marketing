package com.social.marketing.integration.payos.controller;

import com.social.marketing.integration.payos.event.PayOSPaymentEvent;
import com.social.marketing.integration.payos.model.request.PayOSWebhookPayload;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class PayOSWebhookController {

    @Resource
    protected ApplicationEventPublisher publisher;

    @PostMapping("/receive")
    public ResponseEntity<String> handleWebhook(@RequestBody PayOSWebhookPayload payload) {
        publisher.publishEvent(new PayOSPaymentEvent(payload));
        return ResponseEntity.ok("Webhook received successfully");
    }
}
