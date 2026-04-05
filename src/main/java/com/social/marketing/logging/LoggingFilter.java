package com.social.marketing.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * Servlet filter that populates MDC (Mapped Diagnostic Context) with request
 * tracking information.
 * This enables structured logging with traceId, userId, and requestUri in every
 * log entry.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String TRACE_ID_KEY = "traceId";
    private static final String USER_ID_KEY = "userId";
    private static final String REQUEST_URI_KEY = "requestUri";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest) {
            // Generate unique trace ID for this request
            String traceId = UUID.randomUUID().toString();
            MDC.put(TRACE_ID_KEY, traceId);

            // Extract user ID from security context if available
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()
                    && !"anonymousUser".equals(authentication.getPrincipal())) {
                String userId = authentication.getName();
                MDC.put(USER_ID_KEY, userId);
            }

            // Add request URI
            String requestUri = httpRequest.getRequestURI();
            MDC.put(REQUEST_URI_KEY, requestUri);

            // Log the incoming request
            logger.info("Incoming request: {} {}", httpRequest.getMethod(), requestUri);

            try {
                long startTime = System.currentTimeMillis();
                chain.doFilter(request, response);
                long duration = System.currentTimeMillis() - startTime;
                logger.info("Request completed in {}ms", duration);
            } finally {
                // Clean up MDC to prevent memory leaks
                MDC.clear();
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
