package dev.farneser.tasktracker.api.models.permissions;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter

@RequiredArgsConstructor
public enum Role {

    GUEST(Collections.emptySet()),
    USER(
            Set.of(
                    Permission.USER_POST,
                    Permission.USER_GET,
                    Permission.USER_PUT,
                    Permission.USER_PATCH,
                    Permission.USER_DELETE
            )
    ),
    ADMIN(
            Set.of(
                    Permission.ADMIN_POST,
                    Permission.ADMIN_GET,
                    Permission.ADMIN_PUT,
                    Permission.ADMIN_PATCH,
                    Permission.ADMIN_DELETE,

                    Permission.USER_POST,
                    Permission.USER_GET,
                    Permission.USER_PUT,
                    Permission.USER_PATCH,
                    Permission.USER_DELETE
            )
    );

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}