package io.github.angel.raa.service.email.impl;

import io.github.angel.raa.service.email.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender sender;

    public EmailServiceImpl(JavaMailSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendVerificationEmail(String toEmail, String verificationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        String subject = "Verificación de correo electrónico";
        String body = "Por favor, haga clic en el siguiente enlace para verificar su correo electrónico: "
                + "http://localhost:8080/api/v1/verification/verify?token=" + verificationToken;

        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        sender.send(message);
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
