package com.social.marketing.integration.payos.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.marketing.integration.payos.configuration.PayOSProperties;
import com.social.marketing.integration.payos.service.PayOSTransactionService;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PayOSPayOSTransactionServiceImpl implements PayOSTransactionService {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private PayOSProperties properties;

    @Override
    public boolean isValidData(String transaction, String transactionSignature) {
        try {
            JsonNode jsonNode = objectMapper.readTree(transaction);
            Iterator<String> sortedKeys = sortedIterator(jsonNode.fieldNames(), String::compareTo);
            StringBuilder transactionStr = new StringBuilder();
            while (sortedKeys.hasNext()) {
                String key = sortedKeys.next();
                String value = jsonNode.get(key).asText();
                transactionStr.append(key).append('=').append(value);
                if (sortedKeys.hasNext()) {
                    transactionStr.append('&');
                }
            }

            String signature = new HmacUtils("HmacSHA256", properties.getChecksumKey()).hmacHex(transactionStr.toString());
            return signature.equals(transactionSignature);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String generateSignature(Map<String, String> signatureMap) {
        try {
            List<String> sortedKeys = new ArrayList<>(signatureMap.keySet());
            sortedKeys.sort(String::compareTo);

            StringBuilder transactionStr = new StringBuilder();
            for (int i = 0; i < sortedKeys.size(); i++) {
                String key = sortedKeys.get(i);
                String value = signatureMap.get(key);

                transactionStr.append(key).append('=').append(value);
                if (i < sortedKeys.size() - 1) {
                    transactionStr.append('&');
                }
            }
            return new HmacUtils("HmacSHA256", properties.getChecksumKey()).hmacHex(transactionStr.toString());
        } catch (Exception e) {
            return null;
        }
    }


    private Iterator<String> sortedIterator(Iterator<?> it, Comparator<String> comparator) {
        List<String> list = new ArrayList<>();
        while (it.hasNext()) {
            list.add((String) it.next());
        }
        list.sort(comparator);
        return list.iterator();
    }
}
