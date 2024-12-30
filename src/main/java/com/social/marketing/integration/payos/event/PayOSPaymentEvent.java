package com.social.marketing.integration.payos.event;

import com.social.marketing.integration.payos.model.request.PayOSWebhookPayload;

public record PayOSPaymentEvent(PayOSWebhookPayload payload) {
}

