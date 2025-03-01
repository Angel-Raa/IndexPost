package io.github.angel.raa.persistence.repository;

import io.github.angel.raa.persistence.entity.Role;
import io.github.angel.raa.utils.Authorities;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface RoleRepository extends BaseJpaRepository<Role, UUID> {
    Optional<Role> findByAuthorities(final String authorities);

    /**
     * Crear un nuevo rol
     *
     * @param uuid        UUID
     * @param authorities Authorities
     */
    @Transactional
    @Modifying
    @Query("INSERT INTO Role (roleId, authorities) VALUES (:roleId, :authorities)")
    void persist(@Param("roleId") final UUID uuid, @Param("authorities") final Authorities authorities);

}
