package com.social.marketing.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.marketing.auth.ClerkProperties;
import com.social.marketing.user.service.UserService;
import com.svix.Webhook;
import com.svix.exceptions.WebhookVerificationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;
import java.util.function.BiPredicate;

@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
public class ClerkWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(ClerkWebhookController.class);

    private final ClerkProperties clerkProperties;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UserService userService;

    @PostMapping("/clerk")
    public ResponseEntity<String> handleClerkWebhook(
            @RequestHeader MultiValueMap<String, String> headerMap,
            @RequestBody String payload
    ) {
        BiPredicate<String, String> allowAll = (key, value) -> true;
        HttpHeaders headers = HttpHeaders.of(headerMap, allowAll);

        String svixId = headers.firstValue("svix-id").orElse(null);
        String svixTimestamp = headers.firstValue("svix-timestamp").orElse(null);
        String svixSignature = headers.firstValue("svix-signature").orElse(null);

        if (svixId == null || svixTimestamp == null || svixSignature == null) {
            return new ResponseEntity<>("Missing Headers", HttpStatus.BAD_REQUEST);
        }

        try {
            Webhook webhook = new Webhook(clerkProperties.getWebhook().getSecret());
            webhook.verify(payload, headers);
        } catch (WebhookVerificationException e) {
            logger.error("Invalid Webhook Signature: {}", e.getMessage());
            return new ResponseEntity<>("Invalid Signature", HttpStatus.BAD_REQUEST);
        }

        try {
            JsonNode rootNode = objectMapper.readTree(payload);
            String eventType = rootNode.get("type").asText();
            JsonNode data = rootNode.get("data");

            logger.info("Received event type: {}", eventType);

            switch (eventType) {
                case "user.created":
                case "user.updated":
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

    private void handleUserSync(JsonNode data) {
        String clerkUserId = data.path("id").asText();

        String email = null;
        JsonNode emailAddresses = data.path("email_addresses");
        if (emailAddresses.isArray() && !emailAddresses.isEmpty()) {
            email = emailAddresses.get(0).path("email_address").asText();
        } else {
            logger.warn("User {} created without email addresses", clerkUserId);
        }

        String firstName = data.path("first_name").asText(null);
        String lastName = data.path("last_name").asText(null);
        String imageUrl = data.path("image_url").asText(null);

        logger.info("Syncing user: ID={}, Email={}", clerkUserId, email);

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