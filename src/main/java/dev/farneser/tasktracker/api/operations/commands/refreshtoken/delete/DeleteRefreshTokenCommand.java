package dev.farneser.tasktracker.api.operations.commands.refreshtoken.delete;

import dev.farneser.tasktracker.api.mediator.Command;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteRefreshTokenCommand implements Command<Void> {
    @Size(min = 1, max = 255)
    private String token;
}
