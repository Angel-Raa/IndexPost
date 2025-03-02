package io.github.angel.raa.service.auth;

import jakarta.mail.MessagingException;

import java.util.UUID;

public interface AuthenticationVerificationService {
    void sendVerificationEmailToUser(final UUID userId) throws MessagingException;
    boolean verifyUserEmail(String verificationToken);

}
