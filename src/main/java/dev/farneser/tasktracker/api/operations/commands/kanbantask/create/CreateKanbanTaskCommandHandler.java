package dev.farneser.tasktracker.api.operations.commands.kanbantask.create;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.KanbanTask;
import dev.farneser.tasktracker.api.repository.KanbanColumnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateKanbanTaskCommandHandler implements CommandHandler<CreateKanbanTaskCommand, Long> {
    private final KanbanColumnRepository columnRepository;

    @Override
    public Long handle(CreateKanbanTaskCommand command) throws NotFoundException {
        var column = columnRepository.findByIdAndUserId(command.getColumnId(), command.getUserId()).orElseThrow(() -> new NotFoundException("Column with id " + command.getColumnId() + " of user id " + command.getUserId() + "  not found"));

        var orderNumber = 1L;

        if (!column.getTasks().isEmpty()) {
            orderNumber = column.getTasks().get(column.getTasks().size() - 1).getOrderNumber() + 1;
        }

        var task = KanbanTask.builder()
                .taskName(command.getTitle())
                .description(command.getDescription())
                .orderNumber(orderNumber)
                .column(column)
                .user(column.getUser())
                .build();

        column.getTasks().add(task);

        columnRepository.save(column);

        return task.getId();
    }
}
