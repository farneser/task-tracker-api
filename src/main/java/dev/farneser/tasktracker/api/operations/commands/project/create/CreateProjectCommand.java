package dev.farneser.tasktracker.api.operations.commands.project.create;

import dev.farneser.tasktracker.api.mediator.Command;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectCommand implements Command<Long> {
    private Long creatorId;
    @Size(min = 1, max = 255)
    private String projectName;
}
