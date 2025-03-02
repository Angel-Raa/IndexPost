package io.github.angel.raa.persistence.entity;

import io.github.angel.raa.utils.Authorities;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "roles_table")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID roleId;
    @Enumerated(EnumType.STRING)
    private Authorities authorities;

    public Role() {
    }

    public Role(UUID roleId, Authorities authorities) {
        this.roleId = roleId;
        this.authorities = authorities;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public Authorities getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Authorities authorities) {
        this.authorities = authorities;
    }
}
