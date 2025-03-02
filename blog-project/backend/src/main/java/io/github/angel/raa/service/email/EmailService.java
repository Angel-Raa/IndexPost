package io.github.angel.raa.service.email;

public interface EmailService {
    public void sendVerificationEmail(final String toEmail, final String verificationToken);
    public void sendPasswordResetEmail(final String toEmail, final String resetToken);
}
