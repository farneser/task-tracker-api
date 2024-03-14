package dev.farneser.tasktracker.api.operations.commands.task.delete;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.KanbanTask;
import dev.farneser.tasktracker.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteTaskCommandHandler implements CommandHandler<DeleteTaskCommand, Void> {
    private final TaskRepository taskRepository;

    @Override
    public Void handle(DeleteTaskCommand command) throws NotFoundException {
        KanbanTask task = taskRepository.findByIdAndUserId(command.getTaskId(), command.getUserId()).orElseThrow(() -> new NotFoundException("Task with id " + command.getTaskId() + " not found"));

        log.debug("Task found: {}", task);

        List<KanbanTask> tasks = taskRepository.findByUserIdOrderByOrderNumber(command.getUserId()).orElse(new ArrayList<>());

        log.debug("Tasks found: {}", tasks);

        List<KanbanTask> tasksToUpdate = tasks.stream().filter(t -> t.getOrderNumber() > task.getOrderNumber()).toList();

        log.debug("Tasks to update: {}", tasksToUpdate);

        tasksToUpdate.forEach(t -> t.setOrderNumber(t.getOrderNumber() - 1));

        log.debug("Tasks to update after: {}", tasksToUpdate);

        taskRepository.saveAll(tasksToUpdate);

        log.debug("Tasks updated");

        taskRepository.delete(task);

        log.debug("Task deleted: {}", command.getTaskId());

        return null;
    }
}
