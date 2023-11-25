package dev.farneser.tasktracker.api.operations.commands.task.patch;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.repository.ColumnRepository;
import dev.farneser.tasktracker.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PatchTaskCommandHandler implements CommandHandler<PatchTaskCommand, Void> {
    private final ColumnRepository columnRepository;
    private final TaskRepository taskRepository;

    @Override
    public Void handle(PatchTaskCommand command) throws NotFoundException {
        var task = taskRepository.findByIdAndUserId(command.getTaskId(), command.getUserId()).orElseThrow(() -> new NotFoundException("Task with id " + command.getTaskId() + " not found"));

        if (command.getColumnId() != null) {
            if (command.getColumnId() == -1) {
                task.setColumn(null);
            } else {
                task.setColumn(columnRepository.findByIdAndUserId(command.getColumnId(), command.getUserId()).orElseThrow(() -> new NotFoundException("Column with id " + command.getColumnId() + " not found")));
            }
        }

        if (command.getOrderNumber() != null) {
            if (task.getColumn() != null) {
                var oldOrder = task.getOrderNumber() == null ? -1L : task.getOrderNumber();
                long newOrder = command.getOrderNumber();

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
            } else {
                task.setOrderNumber(null);
            }
        }

        if (command.getTaskName() != null) {
            task.setTaskName(command.getTaskName());
        }

        if (command.getDescription() != null) {
            task.setDescription(command.getDescription());
        }

        taskRepository.save(task);

        return null;
    }
}
