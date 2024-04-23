package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ValidationException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.project.create.CreateProjectCommand;
import dev.farneser.tasktracker.api.operations.commands.project.delete.DeleteProjectCommand;
import dev.farneser.tasktracker.api.operations.commands.project.deletemember.DeleteProjectMemberCommand;
import dev.farneser.tasktracker.api.operations.commands.project.leave.LeaveProjectCommand;
import dev.farneser.tasktracker.api.operations.commands.project.patch.PatchProjectCommand;
import dev.farneser.tasktracker.api.operations.commands.project.patchmember.PatchProjectMemberCommand;
import dev.farneser.tasktracker.api.operations.queries.project.getbyid.GetProjectByIdQuery;
import dev.farneser.tasktracker.api.operations.queries.project.getbyuserid.GetProjectByUserIdQuery;
import dev.farneser.tasktracker.api.operations.queries.project.getmemberbyid.GetProjectMemberByIdQuery;
import dev.farneser.tasktracker.api.operations.queries.project.getmembers.GetProjectMembersQuery;
import dev.farneser.tasktracker.api.operations.queries.task.getbyuseridandprojectid.GetTaskByUserIdAndProjectIdQuery;
import dev.farneser.tasktracker.api.operations.views.ProjectMemberView;
import dev.farneser.tasktracker.api.operations.views.ProjectView;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.operations.views.task.TaskLookupView;
import dev.farneser.tasktracker.api.service.auth.UserAuthentication;
import dev.farneser.tasktracker.api.web.dto.project.CreateProjectDto;
import dev.farneser.tasktracker.api.web.dto.project.PatchProjectDto;
import dev.farneser.tasktracker.api.web.dto.project.PatchProjectMemberDto;
import dev.farneser.tasktracker.api.web.models.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for performing operations with projects.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final Mediator mediator;
    private final ModelMapper modelMapper;
    private final UserService userService;

    /**
     * Creates a new project.
     *
     * @param dto            Data for creating the project.
     * @param authentication Authentication data of the user.
     * @return View of the created project.
     * @throws NotFoundException               If the user is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public ProjectView create(CreateProjectDto dto, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {

        UserView user = userService.getUser(authentication);

        CreateProjectCommand command = modelMapper.map(dto, CreateProjectCommand.class);

        command.setCreatorId(user.getId());

        Long projectId = mediator.send(command);

        return get(projectId, authentication);
    }

    /**
     * Gets a list of user's projects.
     *
     * @param authentication Authentication data of the user.
     * @return List of user's project views.
     * @throws NotFoundException               If the user is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public List<ProjectView> get(UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        return mediator.send(new GetProjectByUserIdQuery(user.getId()));
    }

    /**
     * Gets a project by its identifier.
     *
     * @param id             Project identifier.
     * @param authentication Authentication data of the user.
     * @return Project view.
     * @throws NotFoundException               If the project is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public ProjectView get(Long id, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        return mediator.send(new GetProjectByIdQuery(user.getId(), id));
    }

    /**
     * Deletes a project by its identifier.
     *
     * @param id             Project identifier.
     * @param authentication Authentication data of the user.
     * @throws NotFoundException               If the project is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public void delete(Long id, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        UserView user = userService.getUser(authentication);

        mediator.send(new DeleteProjectCommand(user.getId(), id));
    }

    /**
     * Modifies project data.
     *
     * @param id              Project identifier.
     * @param patchProjectDto Data for modifying the project.
     * @param authentication  Authentication data of the user.
     * @return View of the modified project.
     * @throws NotFoundException               If the project is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public ProjectView patch(Long id, PatchProjectDto patchProjectDto, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        UserView user = userService.getUser(authentication);

        PatchProjectCommand command = modelMapper.map(patchProjectDto, PatchProjectCommand.class);

        command.setProjectId(id);
        command.setUserId(user.getId());

        mediator.send(command);

        return get(id, authentication);
    }

    /**
     * Gets tasks for a project.
     *
     * @param id             Project identifier.
     * @param authentication Authentication data of the user.
     * @return List of task views.
     * @throws NotFoundException               If the project is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public List<TaskLookupView> getTasks(Long id, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        return mediator.send(new GetTaskByUserIdAndProjectIdQuery(user.getId(), id));
    }

    /**
     * Gets members of a project.
     *
     * @param id             Project identifier.
     * @param authentication Authentication data of the user.
     * @return List of project member views.
     * @throws NotFoundException               If the project is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public List<ProjectMemberView> getMembers(Long id, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        return mediator.send(new GetProjectMembersQuery(user.getId(), id));
    }

    /**
     * Allows a user to leave a project.
     *
     * @param id             Project identifier.
     * @param authentication Authentication data of the user.
     * @return Success message.
     * @throws NotFoundException               If the project is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public Message leaveProject(Long id, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        UserView user = userService.getUser(authentication);

        LeaveProjectCommand command = new LeaveProjectCommand(user.getId(), id);

        mediator.send(command);

        return null;
    }

    /**
     * Modifies a project member's role.
     *
     * @param id                    Project identifier.
     * @param patchProjectMemberDto Data for modifying the project member.
     * @param authentication        Authentication data of the user.
     * @return View of the modified project member.
     * @throws NotFoundException               If the project member is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public ProjectMemberView patchMember(Long id, PatchProjectMemberDto patchProjectMemberDto, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        UserView user = userService.getUser(authentication);

        PatchProjectMemberCommand command = modelMapper.map(patchProjectMemberDto, PatchProjectMemberCommand.class);

        command.setUserId(user.getId());
        command.setProjectId(id);

        Long projectMemberId = mediator.send(command);

        return mediator.send(new GetProjectMemberByIdQuery(projectMemberId, user.getId(), id));
    }

    /**
     * Deletes a member from a project.
     *
     * @param id             Project identifier.
     * @param memberId       Member identifier.
     * @param authentication Authentication data of the user.
     * @throws NotFoundException               If the project or member is not found.
     * @throws OperationNotAuthorizedException If the operation is not authorized.
     */
    public void deleteMember(Long id, Long memberId, UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        UserView user = userService.getUser(authentication);
        mediator.send(new DeleteProjectMemberCommand(user.getId(), memberId, id));
    }
}