package dev.farneser.tasktracker.api.operations.commands.user.activate;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivateUserCommand implements Command<Long> {
    private String email;
}
