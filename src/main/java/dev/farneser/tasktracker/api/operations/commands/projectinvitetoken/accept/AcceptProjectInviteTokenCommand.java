package dev.farneser.tasktracker.api.operations.commands.projectinvitetoken.accept;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcceptProjectInviteTokenCommand implements Command<Long> {
    private Long userId;
    private String token;
}
