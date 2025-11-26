package com.social.marketing.user.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.marketing.user.service.UserService;
import com.svix.Webhook;
import com.svix.exceptions.WebhookVerificationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;

@RestController
@RequestMapping("/api/webhooks")
@AllArgsConstructor
public class ClerkWebhookController {

    // Lấy secret key từ application.properties
    @Value("${clerk.webhook.secret}")
    private final String WEBHOOK_SECRET = "";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UserService userService;

    @PostMapping("/clerk")
    public ResponseEntity<String> handleClerkWebhook(
            @RequestHeader HttpHeaders headers,
            @RequestBody String payload
    ) {
        String svixId = headers.firstValue("svix-id").orElse(null);
        String svixTimestamp = headers.firstValue("svix-timestamp").orElse(null);
        String svixSignature = headers.firstValue("svix-signature").orElse(null);

        if (svixId == null || svixTimestamp == null || svixSignature == null) {
            return new ResponseEntity<>("Missing Headers", HttpStatus.BAD_REQUEST);
        }

        // 2. Xác thực Webhook (Verify Signature)
        try {
            Webhook webhook = new Webhook(WEBHOOK_SECRET);
            // Phương thức verify sẽ ném Exception nếu chữ ký không khớp
            webhook.verify(payload, headers);
        } catch (WebhookVerificationException e) {
            return new ResponseEntity<>("Invalid Signature", HttpStatus.BAD_REQUEST);
        }

        // 3. Xử lý dữ liệu nghiệp vụ (Sync User)
        try {
            JsonNode rootNode = objectMapper.readTree(payload);
            String eventType = rootNode.get("type").asText();

            if ("user.created".equals(eventType)) {
                JsonNode data = rootNode.get("data");

                // Lấy thông tin user
                String clerkUserId = data.get("id").asText();
                String email = data.get("email_addresses").get(0).get("email_address").asText();

                System.out.println("Syncing user: " + email);
//                userService.syncUser(clerkUserId, email, ...);
            }

            // Xử lý thêm user.updated hoặc user.deleted nếu cần

            return new ResponseEntity<>("Webhook processed", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error processing payload", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}