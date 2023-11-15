package dev.farneser.tasktracker.api.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterRequest {
    @NonNull
    @Email(message = "You should enter email like 'example@email.com'")
    private String email;
    @NonNull
    @Length(min = 8, max = 64)
    private String password;
    @NonNull
    @Length(min = 8, max = 64)
    @JsonProperty("confirm_password")
    private String confirmPassword;
}
