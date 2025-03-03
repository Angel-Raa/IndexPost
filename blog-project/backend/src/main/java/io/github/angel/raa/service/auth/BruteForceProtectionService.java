package io.github.angel.raa.service.auth;

/**
 * This interface defines the contract for a brute force protection service.
 * It provides methods to handle authentication failures, successful authentications,
 */
public interface BruteForceProtectionService {
    void onAuthenticationFailure(final String email);
    void onAuthenticationSuccess(final String email);

}
