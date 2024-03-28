package dev.farneser.tasktracker.api.operations.commands.column.create;

import dev.farneser.tasktracker.api.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateColumnCommand implements Command<Long> {
    private Long userId;
    private String columnName;
    private Boolean isCompleted;
    private Long projectId;
}
