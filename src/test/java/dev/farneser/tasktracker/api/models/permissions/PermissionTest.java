package dev.farneser.tasktracker.api.models.permissions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PermissionTest {

    @Test
    public void testAdminPermissions() {
        assertEquals("admin:post", Permission.ADMIN_POST.getPermission());
        assertEquals("admin:get", Permission.ADMIN_GET.getPermission());
        assertEquals("admin:put", Permission.ADMIN_PUT.getPermission());
        assertEquals("admin:patch", Permission.ADMIN_PATCH.getPermission());
        assertEquals("admin:delete", Permission.ADMIN_DELETE.getPermission());
    }

    @Test
    public void testUserPermissions() {
        assertEquals("user:post", Permission.USER_POST.getPermission());
        assertEquals("user:get", Permission.USER_GET.getPermission());
        assertEquals("user:put", Permission.USER_PUT.getPermission());
        assertEquals("user:patch", Permission.USER_PATCH.getPermission());
        assertEquals("user:delete", Permission.USER_DELETE.getPermission());
    }
}
