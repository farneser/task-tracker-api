package dev.farneser.tasktracker.api.operations.commands.project.patch;

import dev.farneser.tasktracker.api.mediator.Command;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchProjectCommand implements Command<Void> {
    private Long userId;
    private Long projectId;
    @Size(min = 1, max = 255)
    private String projectName;
}
