package dev.farneser.tasktracker.api.operations.commands.project.delete;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteProjectCommand implements Command<Void> {
    private Long memberId;
    private Long projectId;
}
