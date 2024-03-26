package dev.farneser.tasktracker.api.models;

import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
public class ConfirmEmailToken implements Serializable {
    @Id
    private final UUID token;
    private final String email;
    private final Date expiresAt;

    public ConfirmEmailToken(String email, Date expiresAt) {
        this.email = email;
        this.token = UUID.randomUUID();
        this.expiresAt = expiresAt;
    }
}
