package com.social.marketing.auth.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.marketing.auth.entity.Role;
import com.social.marketing.auth.entity.Token;
import com.social.marketing.auth.entity.User;
import com.social.marketing.auth.entity.type.TokenType;
import com.social.marketing.auth.jwt.JwtService;
import com.social.marketing.auth.model.request.LoginRequest;
import com.social.marketing.auth.model.request.SignupRequest;
import com.social.marketing.auth.model.response.AuthResponse;
import com.social.marketing.auth.repository.RoleRepository;
import com.social.marketing.auth.repository.TokenRepository;
import com.social.marketing.auth.repository.UserRepository;
import com.social.marketing.auth.service.AuthService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private TokenRepository tokenRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtService jwtService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public AuthResponse signup(SignupRequest request) {
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();
        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(UserDetailsImpl.build(savedUser));
        String refreshToken = jwtService.generateRefreshToken(UserDetailsImpl.build(savedUser));
        saveUserToken(savedUser, jwtToken);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponse token(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String token = jwtService.generateToken(UserDetailsImpl.build(user));
        String refreshToken = jwtService.generateRefreshToken(UserDetailsImpl.build(user));
        revokeAllUserTokens(user);
        saveUserToken(user, token);
        return AuthResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, UserDetailsImpl.build(user))) {
                var accessToken = jwtService.generateToken(UserDetailsImpl.build(user));
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
