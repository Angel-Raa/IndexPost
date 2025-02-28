package io.github.angel.raa.persistence.repository;

import io.github.angel.raa.persistence.entity.Token;
import io.github.angel.raa.persistence.entity.User;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends BaseJpaRepository<Token, UUID> {
    Optional<Token> findByTokenValue(final String tokenValue);
    List<Token> findAllByUserAndExpiredFalseAndRevokedFalse(User user);
    void deleteByExpiresAtBefore(LocalDateTime now);
}
