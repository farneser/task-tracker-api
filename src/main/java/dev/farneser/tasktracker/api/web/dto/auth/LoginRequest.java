package dev.farneser.tasktracker.api.web.dto.auth;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class LoginRequest {
    @Email(message = "You should enter email like 'example@email.com'")
    private String email;
    private String password;
}