package dev.farneser.tasktracker.api.operations.commands.project.patchmember;

import dev.farneser.tasktracker.api.mediator.Command;
import dev.farneser.tasktracker.api.models.project.ProjectRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchProjectMemberCommand implements Command<Long> {
    private Long userId;
    private Long memberId;
    private Long projectId;
    private ProjectRole role;
}
