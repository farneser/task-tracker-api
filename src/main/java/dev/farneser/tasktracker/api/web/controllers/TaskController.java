package dev.farneser.tasktracker.api.web.controllers;


import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.operations.views.TaskView;
import dev.farneser.tasktracker.api.service.TaskService;
import dev.farneser.tasktracker.api.web.dto.task.CreateTaskDto;
import dev.farneser.tasktracker.api.web.dto.task.PatchTaskDto;
import dev.farneser.tasktracker.api.web.models.Message;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "Get tasks", description = "Get tasks by JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got tasks"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
    })
    public ResponseEntity<List<TaskView>> get(Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(taskService.get(authentication));
    }

    @GetMapping("archive")
    @Operation(summary = "Get archived tasks", description = "Get archived tasks by JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got archived tasks"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
    })
    public ResponseEntity<List<TaskView>> getArchived(Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(taskService.getArchived(authentication));
    }

    @PostMapping
    @Operation(summary = "Create task", description = "Create task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created task"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Column not found")
    })
    public ResponseEntity<TaskView> create(
            @RequestBody @Valid CreateTaskDto createTaskDto,
            Authentication authentication
    ) throws NotFoundException {
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
            Authentication authentication
    ) throws NotFoundException {
        return ResponseEntity.ok(taskService.get(id, authentication));
    }

    @PatchMapping("{id}")
    @Operation(summary = "Patch task", description = "Patch task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully patched task"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Task not found or Column not found")
    })
    public ResponseEntity<TaskView> patchById(
            @PathVariable Long id,
            @RequestBody @Valid PatchTaskDto patchTaskDto,
            Authentication authentication
    ) throws NotFoundException {
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
            Authentication authentication
    ) throws NotFoundException {
        taskService.delete(id, authentication);

        return ResponseEntity.ok(Message.body("Successfully deleted column"));
    }

    @PutMapping("/archive")
    @Operation(summary = "Archive tasks", description = "Archive tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully archived tasks"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
    })
    public ResponseEntity<Message> archieTasks(Authentication authentication) throws NotFoundException {
        taskService.archiveTasks(authentication);

        return ResponseEntity.ok(Message.body("Successfully archived tasks"));
    }
}
