package dev.farneser.tasktracker.api.models.permissions;

import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RoleTest {

    @Test
    public void testGuestRole() {
        Role guestRole = Role.GUEST;

        assertTrue(guestRole.getPermissions().isEmpty());

        List<SimpleGrantedAuthority> authorities = guestRole.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_GUEST")));
    }

    @Test
    public void testUserRole() {
        Role userRole = Role.USER;

        Set<Permission> userPermissions = Set.of(
                Permission.USER_POST,
                Permission.USER_GET,
                Permission.USER_PUT,
                Permission.USER_PATCH,
                Permission.USER_DELETE
        );

        assertEquals(userPermissions, userRole.getPermissions());

        List<SimpleGrantedAuthority> authorities = userRole.getAuthorities();

        assertEquals(userPermissions.size() + 1, authorities.size());
        assertTrue(authorities.containsAll(userPermissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .toList()));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void testAdminRole() {
        Role adminRole = Role.ADMIN;

        Set<Permission> adminPermissions = Set.of(
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
        );

        assertEquals(adminPermissions, adminRole.getPermissions());

        List<SimpleGrantedAuthority> authorities = adminRole.getAuthorities();

        assertEquals(adminPermissions.size() + 1, authorities.size());
        assertTrue(authorities.containsAll(adminPermissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .toList()));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}
