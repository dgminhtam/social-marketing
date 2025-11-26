package com.social.marketing.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.marketing.user.service.UserService;
import com.svix.Webhook;
import com.svix.exceptions.WebhookVerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;
import java.util.function.BiPredicate;

@RestController
@RequestMapping("/api/webhooks")
public class ClerkWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(ClerkWebhookController.class);

    @Value("${clerk.webhook.secret}")
    private String WEBHOOK_SECRET;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserService userService;

    @PostMapping("/clerk")
    public ResponseEntity<String> handleClerkWebhook(
            @RequestHeader MultiValueMap<String, String> headerMap,
            @RequestBody String payload
    ) {
        // 1. Chuyển đổi Headers để Svix hiểu
        BiPredicate<String, String> allowAll = (key, value) -> true;
        HttpHeaders headers = HttpHeaders.of(headerMap, allowAll);

        String svixId = headers.firstValue("svix-id").orElse(null);
        String svixTimestamp = headers.firstValue("svix-timestamp").orElse(null);
        String svixSignature = headers.firstValue("svix-signature").orElse(null);

        if (svixId == null || svixTimestamp == null || svixSignature == null) {
            return new ResponseEntity<>("Missing Headers", HttpStatus.BAD_REQUEST);
        }

        // 2. Xác thực Webhook (Verify Signature)
        try {
            Webhook webhook = new Webhook(WEBHOOK_SECRET);
            webhook.verify(payload, headers);
        } catch (WebhookVerificationException e) {
            logger.error("Invalid Webhook Signature: {}", e.getMessage());
            return new ResponseEntity<>("Invalid Signature", HttpStatus.BAD_REQUEST);
        }

        // 3. Xử lý dữ liệu nghiệp vụ
        try {
            JsonNode rootNode = objectMapper.readTree(payload);
            String eventType = rootNode.get("type").asText();
            JsonNode data = rootNode.get("data");

            logger.info("Received event type: {}", eventType);

            switch (eventType) {
                case "user.created":
                case "user.updated": // Thường logic update cũng tương tự create (upsert)
                    handleUserSync(data);
                    break;

                case "user.deleted":
                    handleUserDelete(data);
                    break;

                default:
                    logger.warn("Unhandled event type: {}", eventType);
            }

            return new ResponseEntity<>("Webhook processed", HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error processing webhook payload", e);
            return new ResponseEntity<>("Error processing payload", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Tách logic ra hàm riêng cho gọn
    private void handleUserSync(JsonNode data) {
        String clerkUserId = data.path("id").asText();

        // Xử lý Email an toàn (tránh lỗi IndexOutOfBounds nếu mảng rỗng)
        String email = null;
        JsonNode emailAddresses = data.path("email_addresses");
        if (emailAddresses.isArray() && !emailAddresses.isEmpty()) {
            // Lấy email đầu tiên
            email = emailAddresses.get(0).path("email_address").asText();
        } else {
            // Fallback nếu không có email (ví dụ đăng ký bằng Phone)
            logger.warn("User {} created without email addresses", clerkUserId);
        }

        // Dùng .path() thay vì .get() để tránh null pointer
        String firstName = data.path("first_name").asText(null);
        String lastName = data.path("last_name").asText(null);
        String imageUrl = data.path("image_url").asText(null);

        logger.info("Syncing user: ID={}, Email={}", clerkUserId, email);

        // Gọi Service
        userService.syncUser(clerkUserId, email, firstName, lastName, imageUrl);
    }

    private void handleUserDelete(JsonNode data) {
        String clerkUserId = data.path("id").asText();
        if (data.has("deleted") && data.get("deleted").asBoolean()) {
            logger.info("Deleting user: {}", clerkUserId);
            // userService.deleteUser(clerkUserId);
        }
    }
}