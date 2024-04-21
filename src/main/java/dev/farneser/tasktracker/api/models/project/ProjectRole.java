package dev.farneser.tasktracker.api.models.project;


import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public enum ProjectRole {
    MEMBER(
            Set.of(
                    ProjectPermission.USER_POST,
                    ProjectPermission.USER_GET,
                    ProjectPermission.USER_PUT,
                    ProjectPermission.USER_PATCH,
                    ProjectPermission.USER_DELETE
            )
    ),
    ADMIN(
            Set.of(
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
            )
    ),
    CREATOR(
            Set.of(
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
            )
    );

    private final Set<ProjectPermission> permissions;

    public boolean hasPermission(ProjectPermission permission) {
        return permissions.contains(permission);
    }
}