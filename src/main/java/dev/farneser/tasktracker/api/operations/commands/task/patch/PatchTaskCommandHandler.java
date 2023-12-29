package dev.farneser.tasktracker.api.operations.commands.task.patch;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.KanbanTask;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import dev.farneser.tasktracker.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class PatchTaskCommandHandler implements CommandHandler<PatchTaskCommand, Void> {
    private final ColumnRepository columnRepository;
    private final TaskRepository taskRepository;

    private static void patchOrder(PatchTaskCommand command, KanbanTask task) {
        if (command.getOrderNumber() != null) {
            log.debug("Order number changed from {} to {}", task.getOrderNumber(), command.getOrderNumber());

            if (task.getColumn() != null) {
                var oldOrder = task.getOrderNumber() == null ? -1L : task.getOrderNumber();
                long newOrder = command.getOrderNumber();

                log.debug("Old order: {}", oldOrder);

                task.setOrderNumber(newOrder);

                var tasksToChange = task.getColumn().getTasks().stream().filter(c -> c.getOrderNumber() >= Math.min(oldOrder, newOrder) && c.getOrderNumber() <= Math.max(oldOrder, newOrder)).toList();

                log.debug("Tasks to change: {}", tasksToChange);

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
            } else {
                log.debug("Column is null");
                task.setOrderNumber(null);
            }
        }
    }

    @Override
    public Void handle(PatchTaskCommand command) throws NotFoundException {
        var task = taskRepository.findByIdAndUserId(command.getTaskId(), command.getUserId()).orElseThrow(() -> new NotFoundException("Task with id " + command.getTaskId() + " not found"));

        log.debug("Task found: {}", task);

        if (command.getColumnId() != null) {
            log.debug("Column id: {}", command.getColumnId());

            if (command.getColumnId() == -1) {
                log.debug("Column set to null");
                task.setColumn(null);
            } else {
                log.debug("Column set to {}", command.getColumnId());

                task.setColumn(columnRepository.findByIdAndUserId(command.getColumnId(), command.getUserId()).orElseThrow(() -> new NotFoundException("Column with id " + command.getColumnId() + " not found")));
            }
        }

        patchOrder(command, task);

        if (command.getTaskName() != null) {
            log.debug("Task name changed from {} to {}", task.getTaskName(), command.getTaskName());

            task.setTaskName(command.getTaskName());
        }

        if (command.getDescription() != null) {
            log.debug("Description changed from {} to {}", task.getDescription(), command.getDescription());

            task.setDescription(command.getDescription());
        }

        task.setEditDate(new Date(System.currentTimeMillis()));

        log.debug("Task updated: {}", task);

        taskRepository.save(task);

        log.debug("Task saved: {}", task);

        return null;
    }
}
