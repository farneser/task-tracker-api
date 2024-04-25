package dev.farneser.tasktracker.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "LoginRequest", description = "Login request DTO")
public class LoginRequest {
    @Schema(name = "login", description = "Username or email", example = "example@email.com")
    private String login;
    @Schema(name = "password", description = "User password", example = "password", minLength = 8, maxLength = 64)
    private String password;
}