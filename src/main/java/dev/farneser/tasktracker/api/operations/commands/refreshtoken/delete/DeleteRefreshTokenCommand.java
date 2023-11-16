package dev.farneser.tasktracker.api.operations.commands.refreshtoken.delete;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteRefreshTokenCommand implements Command<Void> {
    private String token;
}
