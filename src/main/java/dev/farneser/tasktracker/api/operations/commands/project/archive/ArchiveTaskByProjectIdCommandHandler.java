package dev.farneser.tasktracker.api.operations.commands.project.archive;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.Status;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import dev.farneser.tasktracker.api.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArchiveTaskByProjectIdCommandHandler implements CommandHandler<ArchiveTaskByProjectIdCommand, Void> {
    private final StatusRepository statusRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public static void archive(Status status) {
        log.debug("Status found: {}", status);

        if (status.getIsCompleted() && status.getTasks() != null) {
            status.getTasks().forEach(task -> {
                log.debug("Task found: {}", task);

                task.setStatus(null);
                // FIXME 15.04.2024: mb order number for archive
                task.setOrderNumber(-1L);
            });
        }
    }

    @Override
    public Void handle(ArchiveTaskByProjectIdCommand command)
            throws NotFoundException {

        ProjectMember projects = projectMemberRepository
                .findByProjectIdAndMemberId(command.getProjectId(), command.getUserId())
                .orElseThrow(() -> new NotFoundException(""));


        projects.getProject().getStatuses().forEach(ArchiveTaskByProjectIdCommandHandler::archive);

        log.debug("Tasks archived");

        statusRepository.saveAll(projects.getProject().getStatuses());

        return null;
    }
}
