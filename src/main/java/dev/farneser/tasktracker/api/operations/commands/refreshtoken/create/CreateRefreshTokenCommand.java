package dev.farneser.tasktracker.api.operations.commands.refreshtoken.create;

import dev.farneser.tasktracker.api.mediator.Command;
import dev.farneser.tasktracker.api.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateRefreshTokenCommand implements Command<Long> {
    private String token;
    private Long userId;

    public CreateRefreshTokenCommand(String token, User user) {
        this(token, user.getId());
    }
}
