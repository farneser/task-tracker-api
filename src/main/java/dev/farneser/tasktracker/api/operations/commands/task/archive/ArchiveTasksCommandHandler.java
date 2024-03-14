package dev.farneser.tasktracker.api.operations.commands.task.archive;

import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.KanbanColumn;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArchiveTasksCommandHandler implements CommandHandler<ArchiveTasksCommand, Void> {
    private final ColumnRepository columnRepository;

    @Override
    public Void handle(ArchiveTasksCommand command) {
        List<KanbanColumn> columns = columnRepository.findByUserIdOrderByOrderNumber(command.getUserId()).orElse(new ArrayList<>());

        log.debug("Columns found: {}", columns);

        columns.forEach(column -> {
            log.debug("Column found: {}", column);

            if (column.getIsCompleted() && column.getTasks() != null) {
                column.getTasks().forEach(task -> {
                    log.debug("Task found: {}", task);

                    task.setColumn(null);
                    task.setOrderNumber(-1L);
                });
            }
        });

        log.debug("Tasks archived");

        columnRepository.saveAll(columns);

        return null;
    }
}
