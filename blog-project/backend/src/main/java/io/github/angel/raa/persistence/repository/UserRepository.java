package io.github.angel.raa.persistence.repository;

import io.github.angel.raa.persistence.entity.User;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseJpaRepository<User, UUID>, PagingAndSortingRepository<User, UUID> {
    Optional<User> findByEmail(final String email);
    Optional<User> findByGoogleId(final String googleId);
    boolean existsByEmail(final String email);
    Optional<User> findByName(final String name);
    // Encontrar usuario por token de recuperación de contraseña
    Optional<User> findByTokens_TokenValue(final String tokenValue);
    // Encontrar usuarios no verificados
    boolean existsByIsVerifiedFalse();
    // Encontrar usuarios verificados
    boolean existsByIsVerifiedTrue();


}
