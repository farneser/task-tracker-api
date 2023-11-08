package dev.farneser.tasktracker.api.web.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginRequest {
    @NonNull
    @Email(message = "You should enter email like 'example@email.com'")
    private String email;
    @NonNull
    @Length(min = 8, max = 64)
    private String password;
}