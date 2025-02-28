package io.github.angel.raa.persistence.repository;

import io.github.angel.raa.persistence.entity.User;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
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
    @Query("SELECT u FROM User u JOIN u.tokens t WHERE t.tokenValue = :tokenValue")
    Optional<User> findByTokensTokenValue(final @Param("tokenValue") String tokenValue);
    // Encontrar usuarios no verificados
    boolean existsByIsVerifiedFalse();
    // Encontrar usuarios verificados
    boolean existsByIsVerifiedTrue();


}
