package io.github.angel.raa.service.auth;

public interface AccountUnlockService {
    void unlockAccount(String email);
    void unlockExpiredAccounts();

}
