package io.github.angel.raa.service.auth.impl;

import io.github.angel.raa.excpetion.UserNotFoundException;
import io.github.angel.raa.persistence.entity.Token;
import io.github.angel.raa.persistence.entity.User;
import io.github.angel.raa.persistence.repository.TokenRepository;
import io.github.angel.raa.persistence.repository.UserRepository;
import io.github.angel.raa.service.auth.AuthenticationVerificationService;
import io.github.angel.raa.service.email.EmailService;
import io.github.angel.raa.utils.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationVerificationServiceImpl implements AuthenticationVerificationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationVerificationServiceImpl.class);
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    @Value("${app.token.expiration-verification}")
    private String verificationTokenExpiration;

    public AuthenticationVerificationServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;

    }


    @Transactional
    @Override
    public void sendVerificationEmailToUser(UUID userId) {
        log.info("Sending verification email to user: {}", userId);
        User  user = userRepository.findById(userId).orElseThrow();
        String verificationTokenValue = UUID.randomUUID().toString();
        Token token = new Token();
        token.setTokenValue(verificationTokenValue);
        token.setTokenType(TokenType.VERIFY_EMAIL);
        token.setUserId(userId);
        token.setExpiresAt(LocalDateTime.now().plus(Duration.parse(verificationTokenExpiration)));
        String email = user.getEmail();
        try {
            tokenRepository.persist(token);
            log.info("Token persisted successfully: {}", token.getTokenValue());
        } catch (Exception e) {
            log.error("Failed to persist token for user: {}", userId, e);
            throw new RuntimeException("Failed to persist token", e);
        }

        emailService.sendVerificationEmail(email, verificationTokenValue);

    }

    @Transactional
    @Override
    public boolean verifyUserEmail(final String verificationToken) {
        Optional<Token> optionalToken = tokenRepository.findByTokenValue(verificationToken);
        if(optionalToken.isPresent()){
            Token token = optionalToken.get();
            if(token.getExpiresAt().isAfter(LocalDateTime.now())){
                User user = userRepository.findById(token.getUserId()).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
                user.setVerified(true);
                user.setUserId(user.getUserId());
                userRepository.update(user);
                tokenRepository.delete(token);
                return true;
            }
        }
        return false;
    }
}
