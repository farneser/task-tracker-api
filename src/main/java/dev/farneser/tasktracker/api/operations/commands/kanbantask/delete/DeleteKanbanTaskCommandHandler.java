package dev.farneser.tasktracker.api.operations.commands.kanbantask.delete;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.repository.KanbanTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteKanbanTaskCommandHandler implements CommandHandler<DeleteKanbanTaskCommand, Void> {
    private final KanbanTaskRepository taskRepository;

    @Override
    public Void handle(DeleteKanbanTaskCommand command) throws NotFoundException {
        var column = taskRepository.findByIdAndUserId(command.getTaskId(), command.getUserId()).orElseThrow(() -> new NotFoundException("Task with id " + command.getTaskId() + " not found"));

        taskRepository.delete(column);

        return null;
    }
}
