package dev.farneser.tasktracker.api.operations.commands.project.deletemember;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteProjectMemberCommand implements Command<Void> {
    private Long userId;
    private Long memberId;
    private Long projectId;
}
