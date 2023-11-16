package dev.farneser.tasktracker.api.operations.commands.refreshtoken.deletebyuserid;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteRefreshTokenByUserIdCommand implements Command<Void> {
    private Long userId;
}
