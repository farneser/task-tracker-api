package dev.farneser.tasktracker.api.operations.commands.kanbantask.patch;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.repository.KanbanColumnRepository;
import dev.farneser.tasktracker.api.repository.KanbanTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PatchKanbanTaskCommandHandler implements CommandHandler<PatchKanbanTaskCommand, Void> {
    private final KanbanColumnRepository columnRepository;
    private final KanbanTaskRepository taskRepository;

    @Override
    public Void handle(PatchKanbanTaskCommand command) throws NotFoundException {
        var task = taskRepository.findByIdAndUserId(command.getTaskId(), command.getUserId()).orElseThrow(() -> new NotFoundException("Task with id " + command.getTaskId() + " not found"));

        if (command.getColumnId() != null) {
            task.setColumn(columnRepository.findByIdAndUserId(command.getColumnId(), command.getUserId()).orElseThrow(() -> new NotFoundException("Column with id " + command.getColumnId() + " not found")));
        }

        if (command.getOrderNumber() != null) {
            var oldOrder = task.getOrderNumber();
            var newOrder = command.getOrderNumber();

            task.setOrderNumber(newOrder);

            var tasksToChange = task.getColumn().getTasks().stream().filter(c -> c.getOrderNumber() >= Math.min(oldOrder, newOrder) && c.getOrderNumber() <= Math.max(oldOrder, newOrder)).toList();

            // FIXME: 11/22/23 fix duplicate code with patch column command handler
            tasksToChange.forEach(t -> {
                if (t.getId().equals(task.getId())) {
                    return;
                }

                if (oldOrder < newOrder) {
                    t.setOrderNumber(t.getOrderNumber() - 1);
                } else {
                    t.setOrderNumber(t.getOrderNumber() + 1);
                }
            });
        }

        if (command.getTitle() != null) {
            task.setTaskName(command.getTitle());
        }

        if (command.getDescription() != null) {
            task.setDescription(command.getDescription());
        }

        taskRepository.save(task);

        return null;
    }
}
