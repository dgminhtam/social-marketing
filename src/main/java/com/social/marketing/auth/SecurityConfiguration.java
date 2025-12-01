package com.social.marketing.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties({SecurityProperties.class, ClerkProperties.class})
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {

    private final ClerkProperties clerkProperties;

    // Cache AuthenticationManager để tối ưu hiệu năng (không tạo lại decoder liên tục)
    private final Map<String, AuthenticationManager> authenticationManagers = new ConcurrentHashMap<>();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // SỬA ĐỔI QUAN TRỌNG: Dùng authenticationManagerResolver thay vì jwt()
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationManagerResolver(authenticationManagerResolver())
                )
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/storefront/**", "/health", "/webhooks/**").permitAll()
                        // Chỉ user có role admin (từ metadata) mới vào được backoffice
                        .requestMatchers("/backoffice/**").hasAuthority("ROLE_admin")
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    // Bean Resolver: Điều phối token đến đúng Manager dựa trên Issuer (iss)
    @Bean
    public AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver() {
        return new JwtIssuerAuthenticationManagerResolver(this::getAuthenticationManager);
    }

    // Factory method: Tạo Manager cho từng Issuer và GẮN CONVERTER
    private AuthenticationManager getAuthenticationManager(String issuer) {
        // 1. Kiểm tra Issuer có nằm trong whitelist config không
        List<String> trustedIssuers = clerkProperties.getIssuers();
        if (trustedIssuers != null && !trustedIssuers.contains(issuer)) {
            // Log warning nếu có token từ nguồn lạ
            log.warn("Access denied: Untrusted Issuer {}", issuer);
            return null;
        }

        // 2. Nếu đã có trong cache thì trả về luôn (Singleton pattern)
        return authenticationManagers.computeIfAbsent(issuer, k -> {
            log.info("Initializing AuthenticationManager for issuer: {}", k);

            // Tải Public Key từ Clerk
            JwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(k);

            // Tạo Provider
            JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtDecoder);

            // QUAN TRỌNG NHẤT: Set Converter để map "role" -> "ROLE_admin"
            // Nếu thiếu dòng này, user sẽ login được nhưng bị lỗi 403 Forbidden
            provider.setJwtAuthenticationConverter(jwtAuthenticationConverter());

            return provider::authenticate;
        });
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // Map với key trong Clerk Template: { "role": "{{user.public_metadata.role}}" }
        grantedAuthoritiesConverter.setAuthoritiesClaimName("role");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Đảm bảo allow đủ origin của cả BO và SF
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:3001"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}