package dev.farneser.tasktracker.api.web.controllers;


import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.operations.views.ProjectView;
import dev.farneser.tasktracker.api.operations.views.StatusView;
import dev.farneser.tasktracker.api.operations.views.task.TaskLookupView;
import dev.farneser.tasktracker.api.service.ProjectService;
import dev.farneser.tasktracker.api.service.StatusService;
import dev.farneser.tasktracker.api.web.dto.project.CreateProjectDto;
import dev.farneser.tasktracker.api.web.dto.project.PatchProjectDto;
import dev.farneser.tasktracker.api.web.models.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
            Authentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        return ResponseEntity.ok(projectService.get(authentication));
    }

    @PostMapping
    public ResponseEntity<ProjectView> create(
            @RequestBody @Valid CreateProjectDto createProjectDto,
            Authentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        return ResponseEntity.status(201).body(projectService.create(createProjectDto, authentication));
    }

    @GetMapping("{id}")
    public ResponseEntity<ProjectView> getById(
            @PathVariable Long id,
            Authentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        return ResponseEntity.ok(projectService.get(id, authentication));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ProjectView> patchById(
            @PathVariable Long id,
            @RequestBody @Valid PatchProjectDto patchProjectDto,
            Authentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        return ResponseEntity.ok(projectService.patch(id, patchProjectDto, authentication));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Message> deleteById(
            @PathVariable Long id,
            Authentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        projectService.delete(id, authentication);

        return ResponseEntity.ok(Message.body("Successfully deleted project"));
    }

    @GetMapping("{id}/statuses")
    @Operation(summary = "Get statuses", description = "Get columns by JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got columns"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Columns not found")
    })
    public ResponseEntity<List<StatusView>> getStatuses(
            @PathVariable Long id,
            @Parameter(description = "Toggles inclusion of current task details in the response")
            @RequestParam(defaultValue = "true") Boolean retrieveTasks,
            Authentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        log.info("Getting columns for user {}", authentication.getName());

        return ResponseEntity.ok(statusService.get(id, retrieveTasks, authentication));
    }

    @GetMapping("{id}/tasks")
    @Operation(summary = "Get tasks", description = "Get tasks by column id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got tasks"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Tasks not found")
    })
    public ResponseEntity<List<TaskLookupView>> getArchivedTasksById(
            @PathVariable Long id,
            Authentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {

        return ResponseEntity.ok(projectService.getTasks(id, authentication));
    }
}
