package dev.farneser.tasktracker.api.operations.commands.refreshtoken.create;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateRefreshTokenCommand implements Command<Long> {
    private String token;
    private Long userId;
}
