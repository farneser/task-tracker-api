package dev.farneser.tasktracker.api.operations.commands.task.delete;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteTaskCommandHandler implements CommandHandler<DeleteTaskCommand, Void> {
    private final TaskRepository taskRepository;

    @Override
    public Void handle(DeleteTaskCommand command) throws NotFoundException {
        var column = taskRepository.findByIdAndUserId(command.getTaskId(), command.getUserId()).orElseThrow(() -> new NotFoundException("Task with id " + command.getTaskId() + " not found"));

        taskRepository.delete(column);

        return null;
    }
}
