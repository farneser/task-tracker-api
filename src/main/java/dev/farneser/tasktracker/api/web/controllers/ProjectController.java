package dev.farneser.tasktracker.api.web.controllers;


import dev.farneser.tasktracker.api.dto.Message;
import dev.farneser.tasktracker.api.dto.project.CreateProjectDto;
import dev.farneser.tasktracker.api.dto.project.PatchProjectDto;
import dev.farneser.tasktracker.api.dto.project.PatchProjectMemberDto;
import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ValidationException;
import dev.farneser.tasktracker.api.operations.views.ProjectMemberView;
import dev.farneser.tasktracker.api.operations.views.ProjectView;
import dev.farneser.tasktracker.api.operations.views.StatusView;
import dev.farneser.tasktracker.api.operations.views.task.TaskLookupView;
import dev.farneser.tasktracker.api.service.ProjectService;
import dev.farneser.tasktracker.api.service.StatusService;
import dev.farneser.tasktracker.api.service.auth.UserAuthentication;
import dev.farneser.tasktracker.api.web.miscellaneous.AuthModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(EndpointConstants.PROJECT_ENDPOINT)
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final StatusService statusService;

    @GetMapping
    public ResponseEntity<List<ProjectView>> get(
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        return ResponseEntity.ok(projectService.get(authentication));
    }

    @PostMapping
    public ResponseEntity<ProjectView> create(
            @RequestBody @Valid CreateProjectDto createProjectDto,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        return ResponseEntity.status(201).body(projectService.create(createProjectDto, authentication));
    }

    @GetMapping("{id}")
    public ResponseEntity<ProjectView> getById(
            @PathVariable Long id,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        return ResponseEntity.ok(projectService.get(id, authentication));
    }

    @GetMapping("{id}/members")
    public ResponseEntity<List<ProjectMemberView>> getMembers(
            @PathVariable Long id,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        return ResponseEntity.ok(projectService.getMembers(id, authentication));
    }

    @PatchMapping("{id}/members")
    public ResponseEntity<ProjectMemberView> patchMember(
            @PathVariable Long id,
            @RequestBody PatchProjectMemberDto patchProjectMemberDto,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        return ResponseEntity.ok(projectService.patchMember(id, patchProjectMemberDto, authentication));
    }

    @PostMapping("{id}/members/leave")
    public ResponseEntity<Message> leaveProject(
            @PathVariable Long id,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        projectService.leaveProject(id, authentication);

        return ResponseEntity.ok(Message.body("Successfully leave project"));
    }

    @DeleteMapping("{id}/members/{memberId}")
    public ResponseEntity<Message> deleteProjectMember(
            @PathVariable Long id,
            @PathVariable Long memberId,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        projectService.deleteMember(id, memberId, authentication);

        return ResponseEntity.ok(Message.body("User deleted successfully"));
    }

    @GetMapping("{id}/statuses")
    @Operation(summary = "Get statuses", description = "Get statuses by JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got statuses"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Statuses not found")
    })
    public ResponseEntity<List<StatusView>> getStatuses(
            @PathVariable Long id,
            @Parameter(description = "Toggles inclusion of current task details in the response")
            @RequestParam(defaultValue = "true") Boolean retrieveTasks,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        log.info("Getting statuses for user {}", authentication.getName());

        return ResponseEntity.ok(statusService.get(id, retrieveTasks, authentication));
    }

    @GetMapping("{id}/archive")
    @Operation(summary = "Get statuses", description = "Get statuses by JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got statuses"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Statuses not found")
    })
    public ResponseEntity<List<TaskLookupView>> getArchivedTasks(
            @PathVariable Long id,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        return ResponseEntity.ok(projectService.getArchivedTasks(id, authentication));
    }

    @PutMapping("{id}/archive")
    @Operation(summary = "Get statuses", description = "Get statuses by JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got statuses"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Statuses not found")
    })
    public ResponseEntity<Message> putArchivedTasks(
            @PathVariable Long id,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        projectService.putArchivedTasks(id, authentication);

        return ResponseEntity.ok(Message.body("Successfully archived tasks"));
    }

    @GetMapping("{id}/tasks")
    @Operation(summary = "Get tasks", description = "Get tasks by status id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got tasks"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Tasks not found")
    })
    public ResponseEntity<List<TaskLookupView>> getArchivedTasksById(
            @PathVariable Long id,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {

        return ResponseEntity.ok(projectService.getTasks(id, authentication));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ProjectView> patchById(
            @PathVariable Long id,
            @RequestBody @Valid PatchProjectDto patchProjectDto,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        return ResponseEntity.ok(projectService.patch(id, patchProjectDto, authentication));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Message> deleteById(
            @PathVariable Long id,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        projectService.delete(id, authentication);

        return ResponseEntity.ok(Message.body("Successfully deleted project"));
    }
}
