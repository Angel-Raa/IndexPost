package io.github.angel.raa.service.auth.impl;

import io.github.angel.raa.persistence.repository.UserRepository;
import io.github.angel.raa.service.auth.AccountUnlockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountUnlockServiceImpl implements AccountUnlockService {
    private final UserRepository repository;
    private static final int BLOCK_DURATION_MINUTES = 30;

    public AccountUnlockServiceImpl(UserRepository repository) {
        this.repository = repository;
    }
    @Transactional
    @Override
    public void unlockAccount(String email) {

        repository.unLock(email);

    }
    @Transactional
    //@Scheduled(fixedRate = 1000 * 60 * BLOCK_DURATION_MINUTES)
    @Override
    public void unlockExpiredAccounts() {

    }
}
