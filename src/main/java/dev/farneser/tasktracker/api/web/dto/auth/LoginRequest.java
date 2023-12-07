package dev.farneser.tasktracker.api.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "LoginRequest", description = "Login request DTO")
public class LoginRequest {
    @Email(message = "You should enter email like 'example@email.com'")
    @Schema(name = "email", description = "User email", example = "example@email.com")
    private String email;
    @Schema(name = "password", description = "User password", example = "password", minLength = 8, maxLength = 64)
    private String password;
}