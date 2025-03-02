package io.github.angel.raa.persistence.repository;

import io.github.angel.raa.persistence.entity.Token;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends BaseJpaRepository<Token, UUID>, PagingAndSortingRepository<Token, UUID> {
    Optional<Token> findByTokenValue(final String tokenValue);
    void deleteByExpiresAtBefore(LocalDateTime now);

    /**
     * Revocar un token (logout o invalidación)
     *
     * @param tokenValue tokenValue
     */
    @Modifying
    @Query("UPDATE Token t SET t.expired = true, t.revoked = true WHERE t.tokenValue =:tokenValue")
    void revokeToken(@Param("tokenValue") final String tokenValue);

    /**
     * Eliminar tokens expirados
     */
    @Modifying
    @Query("DELETE FROM Token t WHERE t.expiresAt <  CURRENT_TIMESTAMP")
    void deleteExpiredTokens();

    /**
     * Eliminar tokens revocados
     */
    @Modifying
    @Query("DELETE FROM Token t WHERE t.revoked = true")
    void deleteRevokedTokens();

    /**
     * Renovación de tokens: Buscar refresh token válido
     */
    @Query("SELECT t FROM Token t WHERE t.tokenValue =:refreshToken AND t.expired = false AND t.revoked = false AND t.tokenType = 'REFRESH'")
    Optional<Token> findValidRefreshToken(@Param("refreshToken") final String refreshToken);

}
