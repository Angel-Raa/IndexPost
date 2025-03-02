package io.github.angel.raa.service.auth.impl;

import io.github.angel.raa.excpetion.InvalidTokenException;
import io.github.angel.raa.excpetion.TokenExpiredException;
import io.github.angel.raa.excpetion.UserNotFoundException;
import io.github.angel.raa.persistence.entity.Token;
import io.github.angel.raa.persistence.entity.User;
import io.github.angel.raa.persistence.repository.TokenRepository;
import io.github.angel.raa.persistence.repository.UserRepository;
import io.github.angel.raa.service.auth.AuthenticationVerificationService;
import io.github.angel.raa.service.email.EmailService;
import io.github.angel.raa.utils.TokenType;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthenticationVerificationServiceImpl implements AuthenticationVerificationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationVerificationServiceImpl.class);
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;


    public AuthenticationVerificationServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;

    }

    /**
     * Envia un correo de verificación al usuario
     * @param userId
     * @throws MessagingException
     */

    @Transactional
    @Override
    public void sendVerificationEmailToUser(UUID userId) throws MessagingException {
        log.info("Sending verification email to user: {}", userId);
        User  user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        if(user.isVerified()){
            throw new InvalidTokenException("El usuario ya ha sido verificado");
        }
        log.info("User found: {}", user.getEmail());
        String verificationTokenValue = UUID.randomUUID().toString();
        log.info("Verification token generated: {}", verificationTokenValue);
        // Crear Token
        Token token = new Token();

        token.setTokenValue(verificationTokenValue);
        token.setTokenType(TokenType.VERIFY_EMAIL);
        log.info("Token type: {}", token.getTokenType());
        token.setExpiresAt(LocalDateTime.now().plus(Duration.parse("PT24H")));
        log.info("Token expiration: {}", token.getExpiresAt());
        token.setUserId(user.getUserId());
        log.info("User Id: {}", token.getUserId());
        log.info("Attempting to persist token: {}", token);

        String email = user.getEmail();
        tokenRepository.persist(token);
        log.info("Token persisted successfully: {}", token.getTokenValue());
        emailService.sendVerificationEmail(email, verificationTokenValue);

    }
    @Transactional
    @Override
    public boolean verifyUserEmail(final String verificationToken) {
       Token token = tokenRepository.findByTokenValue(verificationToken).orElseThrow(() -> new InvalidTokenException("Token not found"));
       if(token.getExpiresAt().isBefore(LocalDateTime.now())){
           throw new TokenExpiredException("El Token ha expirado");
       }
       if(token.isRevoked()){
           throw new InvalidTokenException("El Token ha sido revocado");
       }
       User user = userRepository.findById(token.getUserId()).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
       user.setVerified(true);
       userRepository.update(user);
       tokenRepository.delete(token);
       return true;
    }
}
