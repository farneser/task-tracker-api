package dev.farneser.tasktracker.api.operations.commands.user.register;

import dev.farneser.tasktracker.api.mediator.Command;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatches
public class RegisterUserCommand implements Command<Long> {
    @NotEmpty(message = "Username is required")
    @Size(min = 4, message = "Username must be at least 4 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must contain only letters, numbers, and underscores")
    private String username;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters long")
    private String password;

    @NotEmpty(message = "Confirm password is required")
    @Size(min = 8, max = 64, message = "Confirm password must be between 8 and 64 characters long")
    private String confirmPassword;

    private boolean isSubscribed;
}
