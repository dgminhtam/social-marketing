package com.social.marketing.integration.payos.service;

import java.util.Map;

public interface PayOSTransactionService {

    boolean isValidData(String transaction, String transactionSignature);

    String generateSignature(Map<String, String> signatureMap);
}
