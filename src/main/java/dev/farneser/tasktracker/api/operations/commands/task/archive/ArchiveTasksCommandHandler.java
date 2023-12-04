package dev.farneser.tasktracker.api.operations.commands.task.archive;

import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class ArchiveTasksCommandHandler implements CommandHandler<ArchiveTasksCommand, Void> {
    private final ColumnRepository columnRepository;

    @Override
    public Void handle(ArchiveTasksCommand command) {
        var columns = columnRepository.findByUserIdOrderByOrderNumber(command.getUserId()).orElse(new ArrayList<>());

        columns.forEach(column -> {
            column.getTasks().forEach(task -> {
                task.setColumn(null);
            });
        });

        columnRepository.saveAll(columns);

        return null;
    }
}
