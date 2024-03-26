package dev.farneser.tasktracker.api.models.permissions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    ADMIN_POST("admin:post"),
    ADMIN_GET("admin:get"),
    ADMIN_PUT("admin:put"),
    ADMIN_PATCH("admin:patch"),
    ADMIN_DELETE("admin:delete"),

    USER_POST("user:post"),
    USER_GET("user:get"),
    USER_PUT("user:put"),
    USER_PATCH("user:patch"),
    USER_DELETE("user:delete");

    private final String permission;
}