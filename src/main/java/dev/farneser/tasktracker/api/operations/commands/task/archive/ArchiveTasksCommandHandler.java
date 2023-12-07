package dev.farneser.tasktracker.api.operations.commands.task.archive;

import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArchiveTasksCommandHandler implements CommandHandler<ArchiveTasksCommand, Void> {
    private final ColumnRepository columnRepository;

    @Override
    public Void handle(ArchiveTasksCommand command) {
        var columns = columnRepository.findByUserIdOrderByOrderNumber(command.getUserId()).orElse(new ArrayList<>());

        log.debug("Columns found: {}", columns);

        columns.forEach(column -> {
            column.getTasks().forEach(task -> {
                log.debug("Task found: {}", task);
                task.setColumn(null);
            });
        });

        log.debug("Tasks archived");

        columnRepository.saveAll(columns);

        return null;
    }
}
