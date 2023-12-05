package dev.farneser.tasktracker.api.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ConfirmEmailToken implements Serializable {
    @Id
    private String email;
    private UUID token;
    private Date expiresAt;

    public ConfirmEmailToken(String email, Date expiresAt) {
        this.email = email;
        this.token = UUID.randomUUID();
        this.expiresAt = expiresAt;
    }
}
