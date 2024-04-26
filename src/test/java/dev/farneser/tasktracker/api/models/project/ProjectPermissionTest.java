package dev.farneser.tasktracker.api.models.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProjectPermissionTest {

    @Test
    public void testPermissionValues() {
        assertEquals("creator:post", ProjectPermission.CREATOR_POST.getPermission());
        assertEquals("creator:get", ProjectPermission.CREATOR_GET.getPermission());
        assertEquals("creator:put", ProjectPermission.CREATOR_PUT.getPermission());
        assertEquals("creator:patch", ProjectPermission.CREATOR_PATCH.getPermission());
        assertEquals("creator:delete", ProjectPermission.CREATOR_DELETE.getPermission());

        assertEquals("admin:post", ProjectPermission.ADMIN_POST.getPermission());
        assertEquals("admin:get", ProjectPermission.ADMIN_GET.getPermission());
        assertEquals("admin:put", ProjectPermission.ADMIN_PUT.getPermission());
        assertEquals("admin:patch", ProjectPermission.ADMIN_PATCH.getPermission());
        assertEquals("admin:delete", ProjectPermission.ADMIN_DELETE.getPermission());

        assertEquals("user:post", ProjectPermission.USER_POST.getPermission());
        assertEquals("user:get", ProjectPermission.USER_GET.getPermission());
        assertEquals("user:put", ProjectPermission.USER_PUT.getPermission());
        assertEquals("user:patch", ProjectPermission.USER_PATCH.getPermission());
        assertEquals("user:delete", ProjectPermission.USER_DELETE.getPermission());
    }
}
