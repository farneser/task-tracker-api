package dev.farneser.tasktracker.api.operations.commands.status.create;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateStatusCommand implements Command<Long> {
    private Long userId;
    private Long projectId;
    private String statusName;
    private String statusColor;
    private Boolean isCompleted;
}
