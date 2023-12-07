package dev.farneser.tasktracker.api.operations.commands.task.create;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.KanbanTask;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateTaskCommandHandler implements CommandHandler<CreateTaskCommand, Long> {
    private final ColumnRepository columnRepository;

    @Override
    public Long handle(CreateTaskCommand command) throws NotFoundException {
        var column = columnRepository.findByIdAndUserId(command.getColumnId(), command.getUserId()).orElseThrow(() -> new NotFoundException("Column with id " + command.getColumnId() + " of user id " + command.getUserId() + "  not found"));

        log.debug("Column found: {}", column);

        var orderNumber = 1L;

        if (!column.getTasks().isEmpty()) {
            log.debug("Tasks found: {}", column.getTasks());

            orderNumber = column.getTasks().get(column.getTasks().size() - 1).getOrderNumber() + 1;
        }

        var creationDate = new Date(System.currentTimeMillis());

        log.debug("Order number: {}", orderNumber);

        var task = KanbanTask.builder()
                .taskName(command.getTaskName())
                .description(command.getDescription())
                .orderNumber(orderNumber)
                .column(column)
                .user(column.getUser())
                .creatiionDate(creationDate)
                .editDate(creationDate)
                .build();

        log.debug("Task created: {}", task);

        column.getTasks().add(task);

        log.debug("Task added to column: {}", column);

        columnRepository.save(column);

        log.debug("Column saved: {}", column);

        return task.getId();
    }
}
