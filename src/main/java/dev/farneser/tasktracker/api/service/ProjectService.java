package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.project.create.CreateProjectCommand;
import dev.farneser.tasktracker.api.operations.commands.project.delete.DeleteProjectCommand;
import dev.farneser.tasktracker.api.operations.commands.project.patch.PatchProjectCommand;
import dev.farneser.tasktracker.api.operations.queries.project.getbyid.GetProjectByIdQuery;
import dev.farneser.tasktracker.api.operations.queries.project.getbyuserid.GetProjectByUserIdQuery;
import dev.farneser.tasktracker.api.operations.queries.task.getbyuseridandprojectid.GetTaskByUserIdAndProjectIdQuery;
import dev.farneser.tasktracker.api.operations.views.ProjectView;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.operations.views.task.TaskLookupView;
import dev.farneser.tasktracker.api.web.dto.project.CreateProjectDto;
import dev.farneser.tasktracker.api.web.dto.project.PatchProjectDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final Mediator mediator;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public ProjectView create(CreateProjectDto dto, Authentication authentication) throws NotFoundException, OperationNotAuthorizedException {

        UserView user = userService.getUser(authentication);

        CreateProjectCommand command = modelMapper.map(dto, CreateProjectCommand.class);

        command.setCreatorId(user.getId());

        Long projectId = mediator.send(command);

        return get(projectId, authentication);
    }

    public List<ProjectView> get(Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        return mediator.send(new GetProjectByUserIdQuery(user.getId()));
    }

    public ProjectView get(Long id, Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        return mediator.send(new GetProjectByIdQuery(id, user.getId()));
    }

    public void delete(Long id, Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        mediator.send(new DeleteProjectCommand(user.getId(), id));
    }

    public ProjectView patch(Long id, PatchProjectDto patchProjectDto, Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        PatchProjectCommand command = modelMapper.map(patchProjectDto, PatchProjectCommand.class);

        command.setProjectId(id);
        command.setUserId(user.getId());

        mediator.send(command);

        return get(id, authentication);
    }

    public List<TaskLookupView> getTasks(Long id, Authentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        return mediator.send(new GetTaskByUserIdAndProjectIdQuery(user.getId(), id));
    }
}