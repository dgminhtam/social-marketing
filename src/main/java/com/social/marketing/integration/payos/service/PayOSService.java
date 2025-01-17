package com.social.marketing.integration.payos.service;

import com.social.marketing.integration.payos.model.request.PayOSRequestPaymentRequest;
import com.social.marketing.integration.payos.model.response.PayOSRequestPaymentResponse;

public interface PayOSService {

    PayOSRequestPaymentResponse requestPayment(PayOSRequestPaymentRequest request);
}
