package io.github.angel.raa.utils;

import java.util.List;
import java.util.Set;

public enum Authorities {
    USER(Set.of(Permission.MANAGE_USERS)),
    MODERATOR(Set.of(Permission.MANAGE_POSTS, Permission.MANAGE_COMMENTS, Permission.MANAGE_CATEGORIES)),
    ADMIN(Set.of(Permission.ADMIN_ACCESS, Permission.MANAGE_ROLES, Permission.MANAGE_USERS, Permission.MANAGE_POSTS, Permission.MANAGE_CATEGORIES, Permission.MANAGE_COMMENTS, Permission.MANAGE_LIKES, Permission.MANAGE_TAGS));

    private final Set<Permission> permissions;

    public Set<Permission> getPermissions() {
        return permissions;
    }

    Authorities(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
