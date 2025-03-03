package io.github.angel.raa.service.auth.impl;

import io.github.angel.raa.excpetion.LockedException;
import io.github.angel.raa.persistence.repository.UserRepository;
import io.github.angel.raa.service.auth.BruteForceProtectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BruteForceProtectionServiceImpl implements BruteForceProtectionService {
    private static final Logger log = LoggerFactory.getLogger(BruteForceProtectionServiceImpl.class);
    private final int MAX_ATTEMPTS = 5;
    private final int BLOCK_DURATION_MINUTES = 30;
    private final UserRepository repository;

    public BruteForceProtectionServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public void onAuthenticationFailure(String email) {
        int failedAttempts = repository.findFailedAttempts(email);
        log.info("Failed attempts: {}", failedAttempts);
        if(failedAttempts >=  MAX_ATTEMPTS){
            repository.lock(email);
            throw new LockedException("Usuario bloqueado por demasiados intentos fallidos");
        }
        else {
            repository.increaseFailedAttempts(email);

        }


    }
    @Transactional
    @Override
    public void onAuthenticationSuccess(String email) {
        repository.resetFailedAttempts(email);

    }



}
