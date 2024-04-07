package dev.farneser.tasktracker.api.operations.commands.projectinvitetoken.create;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectInviteTokenCommand implements Command<Long> {
    private Long userId;
    private Long projectId;
}
