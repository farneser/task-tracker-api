package dev.farneser.tasktracker.api.models.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ProjectRoleTest {

    @Test
    public void testHasPermissionForMember() {
        ProjectPermission[] memberPermissions = {
                ProjectPermission.USER_POST,
                ProjectPermission.USER_GET,
                ProjectPermission.USER_PUT,
                ProjectPermission.USER_PATCH,
                ProjectPermission.USER_DELETE
        };

        ProjectRole memberRole = ProjectRole.MEMBER;

        for (ProjectPermission permission : memberPermissions) {
            assertTrue(memberRole.hasPermission(permission));
        }

        assertFalse(memberRole.hasPermission(ProjectPermission.ADMIN_POST));
        assertFalse(memberRole.hasPermission(ProjectPermission.ADMIN_GET));
        assertFalse(memberRole.hasPermission(ProjectPermission.ADMIN_PUT));
        assertFalse(memberRole.hasPermission(ProjectPermission.ADMIN_PATCH));
        assertFalse(memberRole.hasPermission(ProjectPermission.ADMIN_DELETE));

        assertFalse(memberRole.hasPermission(ProjectPermission.CREATOR_POST));
        assertFalse(memberRole.hasPermission(ProjectPermission.CREATOR_GET));
        assertFalse(memberRole.hasPermission(ProjectPermission.CREATOR_PUT));
        assertFalse(memberRole.hasPermission(ProjectPermission.CREATOR_PATCH));
        assertFalse(memberRole.hasPermission(ProjectPermission.CREATOR_DELETE));
    }

    @Test
    public void testHasPermissionForAdmin() {
        ProjectPermission[] adminPermissions = {
                ProjectPermission.ADMIN_POST,
                ProjectPermission.ADMIN_GET,
                ProjectPermission.ADMIN_PUT,
                ProjectPermission.ADMIN_PATCH,
                ProjectPermission.ADMIN_DELETE,
                ProjectPermission.USER_POST,
                ProjectPermission.USER_GET,
                ProjectPermission.USER_PUT,
                ProjectPermission.USER_PATCH,
                ProjectPermission.USER_DELETE
        };

        ProjectRole adminRole = ProjectRole.ADMIN;

        for (ProjectPermission permission : adminPermissions) {
            assertTrue(adminRole.hasPermission(permission));
        }

        assertFalse(adminRole.hasPermission(ProjectPermission.CREATOR_POST));
        assertFalse(adminRole.hasPermission(ProjectPermission.CREATOR_GET));
        assertFalse(adminRole.hasPermission(ProjectPermission.CREATOR_PUT));
        assertFalse(adminRole.hasPermission(ProjectPermission.CREATOR_PATCH));
        assertFalse(adminRole.hasPermission(ProjectPermission.CREATOR_DELETE));
    }

    @Test
    public void testHasPermissionForCreator() {
        ProjectPermission[] creatorPermissions = {
                ProjectPermission.CREATOR_POST,
                ProjectPermission.CREATOR_GET,
                ProjectPermission.CREATOR_PUT,
                ProjectPermission.CREATOR_PATCH,
                ProjectPermission.CREATOR_DELETE,
                ProjectPermission.ADMIN_POST,
                ProjectPermission.ADMIN_GET,
                ProjectPermission.ADMIN_PUT,
                ProjectPermission.ADMIN_PATCH,
                ProjectPermission.ADMIN_DELETE,
                ProjectPermission.USER_POST,
                ProjectPermission.USER_GET,
                ProjectPermission.USER_PUT,
                ProjectPermission.USER_PATCH,
                ProjectPermission.USER_DELETE
        };

        ProjectRole creatorRole = ProjectRole.CREATOR;

        for (ProjectPermission permission : creatorPermissions) {
            assertTrue(creatorRole.hasPermission(permission));
        }
    }
}
