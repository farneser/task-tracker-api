package dev.farneser.tasktracker.api.web.controllers;


import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ValidationException;
import dev.farneser.tasktracker.api.operations.views.task.TaskLookupView;
import dev.farneser.tasktracker.api.operations.views.task.TaskView;
import dev.farneser.tasktracker.api.service.TaskService;
import dev.farneser.tasktracker.api.service.auth.UserAuthentication;
import dev.farneser.tasktracker.api.dto.task.CreateTaskDto;
import dev.farneser.tasktracker.api.dto.task.PatchTaskDto;
import dev.farneser.tasktracker.api.web.miscellaneous.AuthModel;
import dev.farneser.tasktracker.api.dto.Message;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping(EndpointConstants.TASK_ENDPOINT)
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "Get tasks", description = "Get tasks by JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got tasks"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
    })
    public ResponseEntity<List<TaskLookupView>> get(
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        log.info("Getting tasks for user {}", authentication.getName());

        return ResponseEntity.ok(taskService.get(authentication));
    }

    @GetMapping("archive")
    @Operation(summary = "Get archived tasks", description = "Get archived tasks by JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got archived tasks"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
    })
    public ResponseEntity<List<TaskLookupView>> getArchived(
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        log.info("Getting archived tasks for user {}", authentication.getName());

        return ResponseEntity.ok(taskService.getArchived(authentication));
    }

    @PostMapping
    @Operation(summary = "Create task", description = "Create task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created task"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Status not found")
    })
    public ResponseEntity<TaskView> create(
            @RequestBody @Valid CreateTaskDto createTaskDto,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        log.info("Creating task for user {}, task name: {}", authentication.getName(), createTaskDto.getTaskName());

        return ResponseEntity.status(201).body(taskService.create(createTaskDto, authentication));
    }

    @GetMapping("{id}")
    @Operation(summary = "Get task", description = "Get task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got task"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<TaskView> getById(
            @PathVariable Long id,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        log.info("Getting task for user {}, task id: {}", authentication.getName(), id);

        return ResponseEntity.ok(taskService.get(id, authentication));
    }

    @PatchMapping("{id}")
    @Operation(summary = "Patch task", description = "Patch task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully patched task"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Task not found or status not found")
    })
    public ResponseEntity<TaskView> patchById(
            @PathVariable Long id,
            @RequestBody @Valid PatchTaskDto patchTaskDto,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        log.info("Patching task for user {}, task id: {}", authentication.getName(), id);

        return ResponseEntity.ok(taskService.patch(id, patchTaskDto, authentication));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete task", description = "Delete task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted task"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<Message> deleteById(
            @PathVariable Long id,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        log.info("Deleting task for user {}, task id: {}", authentication.getName(), id);

        taskService.delete(id, authentication);

        return ResponseEntity.ok(Message.body("Successfully deleted status"));
    }

    @PutMapping("/archive")
    @Operation(summary = "Archive tasks", description = "Archive tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully archived tasks"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
    })
    public ResponseEntity<Message> archieTasks(
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        log.info("Archiving tasks for user {}", authentication.getName());

        taskService.archiveTasks(authentication);

        return ResponseEntity.ok(Message.body("Successfully archived tasks"));
    }
}
