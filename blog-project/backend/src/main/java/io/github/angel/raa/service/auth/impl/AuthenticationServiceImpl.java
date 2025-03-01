package io.github.angel.raa.service.auth.impl;

import io.github.angel.raa.dto.auth.Login;
import io.github.angel.raa.dto.auth.Register;
import io.github.angel.raa.excpetion.EmailAlreadyExistsException;
import io.github.angel.raa.persistence.entity.Role;
import io.github.angel.raa.persistence.entity.Token;
import io.github.angel.raa.persistence.entity.User;
import io.github.angel.raa.persistence.repository.RoleRepository;
import io.github.angel.raa.persistence.repository.TokenRepository;
import io.github.angel.raa.persistence.repository.UserRepository;
import io.github.angel.raa.service.auth.AuthenticationService;
import io.github.angel.raa.service.auth.AuthenticationVerificationService;
import io.github.angel.raa.utils.Authorities;
import io.github.angel.raa.utils.JwtUtils;
import io.github.angel.raa.utils.TokenType;
import io.github.angel.raa.utils.payload.AuthenticationResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final AuthenticationVerificationService authenticationVerificationService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager manager;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(AuthenticationVerificationService authenticationVerificationService, JwtUtils jwtUtils, UserRepository userRepository, TokenRepository tokenRepository, RoleRepository roleRepository, AuthenticationManager manager, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationVerificationService = authenticationVerificationService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
        this.manager = manager;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public AuthenticationResponse register(Register request) {
        log.info("Register: {}", request);
        final String email = request.getEmail();
        final String password = request.getPassword();
        final String name = request.getName();
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // Crear y persistir el rol
        Role role = new Role();
        role.setAuthorities(Authorities.USER);
        roleRepository.persist(role);
        log.info("Role persisted successfully: {}", role.getAuthorities());

        // Crear un nuevo usuario
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setVerified(false);
        user.setRoleId(role.getRoleId());

        // Guardar el usuario en la base de datos
        userRepository.persist(user);

        log.info("User persisted successfully: {}", user.getUserId());

        // TODO: SOLUCIONA BUG
        // Generar tokens de acceso y refresco
        String accessToken = jwtUtils.generateAccessToken(email);
        String refreshToken = jwtUtils.generateRefreshToken(email);
        log.info("Access token generated: {}", accessToken);
        log.info("Refresh token generated: {}", refreshToken);

        // Crear y persistir el token
        Token token = new Token();
        token.setTokenValue(UUID.randomUUID().toString());
        token.setTokenType(TokenType.BEARER);
        token.setRevoked(false);
        token.setExpired(false);
        token.setExpiresAt(LocalDateTime.now().plusHours(1));
        token.setUserId(user.getUserId());

        try {
            log.info("Attempting to persist token: {}", token);
            tokenRepository.persist(token);
            log.info("Token persisted successfully: {}", token.getTokenValue());
        } catch (Exception e) {
            log.error("Failed to persist token: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to persist token", e);
        }

        return new AuthenticationResponse(accessToken, refreshToken, TokenType.BEARER, "Usuario registrado exitosamente.");
    }
    @Override
    public AuthenticationResponse login(Login request) {
        return null;
    }

    @Override
    public AuthenticationResponse verifyEmail(String verificationToken, String userId) {
        return null;
    }

    @Override
    public AuthenticationResponse refreshToken(String refreshToken) {
        return null;
    }

    @Override
    public AuthenticationResponse logout(String refreshToken) {
        return null;
    }
}
