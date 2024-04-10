package dev.farneser.tasktracker.api.operations.commands.user.register;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.Data;

@Data
public class RegisterUserCommand implements Command<Long> {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private boolean isSubscribed;
}
