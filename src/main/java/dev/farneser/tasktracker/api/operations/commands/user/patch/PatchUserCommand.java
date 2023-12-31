package dev.farneser.tasktracker.api.operations.commands.user.patch;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchUserCommand implements Command<Void> {
    private Long userId;
    private Boolean isSubscribed;
}
