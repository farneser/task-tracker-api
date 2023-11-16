package dev.farneser.tasktracker.api.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.farneser.tasktracker.api.annotations.PasswordMatches;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

@Data
@JsonNaming
@PasswordMatches
public class RegisterRequest {
    @NonNull
    @Email(message = "You should enter email like 'example@email.com'")
    private String email;
    @NonNull
    @Length(min = 8, max = 64)
    private String password;
    @Length(min = 8, max = 64)
    @JsonProperty("confirm_password")
    private String confirmPassword;
}
