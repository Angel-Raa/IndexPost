package io.github.angel.raa.persistence.repository;

import io.github.angel.raa.persistence.entity.Role;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface RoleRepository extends BaseJpaRepository<Role, UUID> {
    Optional<Role> findByAuthorities(final String authorities);

}
