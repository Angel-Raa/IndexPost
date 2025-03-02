package io.github.angel.raa.service.email.impl;

import io.github.angel.raa.service.email.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
    @Value("${app.url}")
    private String appUrl;
    private final JavaMailSender sender;
    private final TemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender sender, TemplateEngine templateEngine) {
        this.sender = sender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendVerificationEmail(String toEmail, String verificationToken) throws MessagingException {
        String verificationLink = appUrl + "/api/v1/verification/verify?token=" + verificationToken;
        log.info("Verification link: {}", verificationLink);
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        Context  context = new Context();
        context.setVariable("verificationLink", verificationLink);
        String htmlContent = templateEngine.process("email/verification", context);
        helper.setTo(toEmail);

        helper.setSubject("Verificación de correo electrónico");
        helper.setText(htmlContent, true);
        log.info("Verification email sent successfully to: {}", toEmail);

        try {
            sender.send(message);
            log.info("Verification email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send verification email to: {}", toEmail, e);
            throw e; // Rethrow to handle at a higher level if needed
        }
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        String subject = "Restablecimiento de contraseña";
        String body = "Por favor, haga clic en el siguiente enlace para restablecer su contraseña: "
                + "http://localhost:8080/api/v1/verification/reset-password?token=" + resetToken;
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        sender.send(message);

    }
}
