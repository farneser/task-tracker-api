package dev.farneser.tasktracker.api.operations.commands.task.archive;

import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.Status;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import dev.farneser.tasktracker.api.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArchiveTasksCommandHandler implements CommandHandler<ArchiveTasksCommand, Void> {
    private final StatusRepository statusRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public Void handle(ArchiveTasksCommand command) {

        List<ProjectMember> projects = projectMemberRepository
                .findByMemberId(command.getUserId())
                .orElse(new ArrayList<>());

        List<Status> statuses = new ArrayList<>();

        projects.forEach(p -> statuses.addAll(p.getProject().getStatuses()));

        log.debug("Statuses found: {}", statuses);

        statuses.forEach(status -> {
            log.debug("Status found: {}", status);

            if (status.getIsCompleted() && status.getTasks() != null) {
                status.getTasks().forEach(task -> {
                    log.debug("Task found: {}", task);

                    task.setStatus(null);
                    task.setOrderNumber(-1L);
                });
            }
        });

        log.debug("Tasks archived");

        statusRepository.saveAll(statuses);

        return null;
    }
}
