package dev.farneser.tasktracker.api.operations.commands.project.create;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.UserNotFoundException;
import dev.farneser.tasktracker.api.mediator.CommandHandler;
import dev.farneser.tasktracker.api.models.project.Project;
import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.models.project.ProjectRole;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import dev.farneser.tasktracker.api.repository.ProjectRepository;
import dev.farneser.tasktracker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateProjectCommandHandler implements CommandHandler<CreateProjectCommand, Long> {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;

    @Override
    public Long handle(CreateProjectCommand command) throws NotFoundException {

        Project project = new Project();
        project.setProjectName(command.getProjectName());

        projectRepository.save(project);

        ProjectMember member = new ProjectMember();

        member.setProject(project);
        member.setMember(userRepository.findById(command.getCreatorId()).orElseThrow(() -> new UserNotFoundException(command.getCreatorId())));
        member.setRole(ProjectRole.CREATOR);

        projectMemberRepository.save(member);

        return project.getId();
    }
}
