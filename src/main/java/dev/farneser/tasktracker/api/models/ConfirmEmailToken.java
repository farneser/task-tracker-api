package dev.farneser.tasktracker.api.models;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class ConfirmEmailToken {
    private final String email;
    private final String token;
    private final Date expiresAt;

    private ConfirmEmailToken(String email, Date expiresAt) {
        this.email = email;
        this.token = UUID.randomUUID().toString();
        this.expiresAt = expiresAt;
    }
}
