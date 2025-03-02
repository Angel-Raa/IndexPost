package io.github.angel.raa.service.email;

import jakarta.mail.MessagingException;

public interface EmailService {
     void sendVerificationEmail(final String toEmail, final String verificationToken) throws MessagingException;
     void sendPasswordResetEmail(final String toEmail, final String resetToken);
}
