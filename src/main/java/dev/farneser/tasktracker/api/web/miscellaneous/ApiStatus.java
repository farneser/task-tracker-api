package dev.farneser.tasktracker.api.web.miscellaneous;

import lombok.Getter;

@Getter
public enum ApiStatus {
    SUCCESS(200),
    UNAUTHORIZED(401),
    ACCESS_TOKEN_EXPIRED(4011),
    REFRESH_TOKEN_EXPIRED(4012),
    SERVER_ERROR(500);

    private final Integer code;

    ApiStatus(int code) {
        this.code = code;
    }

}
