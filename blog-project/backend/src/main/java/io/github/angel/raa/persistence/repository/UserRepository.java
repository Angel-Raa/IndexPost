package io.github.angel.raa.persistence.repository;

import io.github.angel.raa.persistence.entity.User;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    boolean existsByIsVerifiedFalse();
    boolean existsByIsVerifiedTrue();


    /**
     * Verificación de correo electrónico
     */
    @Query("UPDATE User u SET u.isVerified = true WHERE u.email =:email")
    boolean verifyEmail(@Param("email") String email);


    /**
     * Verificar si la cuenta está bloqueada
     *
     * @param email correo electrónico
     */
    @Query("SELECT u FROM User u WHERE u.email =:email AND u.accountLocked = true")
    Optional<User> findLockedAccount(@Param("email") final String email);

    /**
     * Resetear los intentos fallidos después de un inicio de sesión exitoso
     *
     * @param email correo electrónico
     */
    @Modifying
    @Query("UPDATE User u SET u.failedAttempts = 0, u.accountLocked = false, u.lockTime = null WHERE u.email =:email")
    void resetFailedAttempts(@Param("email") String email);

    /**
     * Bloquear la cuenta
     */
    @Modifying
    @Query("UPDATE User u SET u.accountLocked = true, u.lockTime = CURRENT_TIMESTAMP() WHERE u.email =:email")
    void lock(@Param("email") String email);

    @Modifying
    @Query("UPDATE User u SET u.accountLocked = true, u.lockTime = CURRENT_TIMESTAMP(), u.failedAttempts = :failedAttempts WHERE u.email = :email")
    void lock(@Param("email") String email, @Param("failedAttempts") int failedAttempts);

    /**
     * Incrementar el contador de intentos fallidos
     *
     * @param email correo electrónico
     */

    @Modifying
    @Query(value = "UPDATE users_table SET failed_attempts = failed_attempts + 1 WHERE email = :email", nativeQuery = true)
    int increaseFailedAttempts(@Param("email") String email);

    /**
     * Desbloquear la cuenta
     *
     * @param email correo electrónico
     */
    @Modifying
    @Query("UPDATE User u SET u.accountLocked = false, u.lockTime = null, u.failedAttempts = 0 WHERE u.email =:email")
    void unLock(@Param("email") String email);

    @Query("SELECT u.failedAttempts FROM User u WHERE u.email =:email")
    int findFailedAttempts(@Param("email") String email);
}
