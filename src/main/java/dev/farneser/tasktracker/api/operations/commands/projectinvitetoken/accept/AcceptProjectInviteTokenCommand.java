package dev.farneser.tasktracker.api.operations.commands.projectinvitetoken.accept;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a command to accept a project invitation token.
 * This command is used to accept an invitation to join a project by providing the user ID and the invitation token.
 * Upon successful execution, this command returns the ID of the project member (not user id).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcceptProjectInviteTokenCommand implements Command<Long> {

    /**
     * The ID of the user accepting the project invitation.
     */
    private Long userId;

    /**
     * The invitation token associated with the project invitation.
     */
    private String token;
}
