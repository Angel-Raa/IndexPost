package io.github.angel.raa.service.auth;

import java.util.UUID;

public interface AuthenticationVerificationService {
    void sendVerificationEmailToUser(final UUID userId);
    boolean verifyUserEmail(String verificationToken);

}
