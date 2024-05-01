package dev.farneser.tasktracker.api.operations.commands.user.activate;

import dev.farneser.tasktracker.api.mediator.Command;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivateUserCommand implements Command<Long> {
    @Size(min = 1, max = 255)
    @Email(message = "Invalid email format")
    private String email;
}
