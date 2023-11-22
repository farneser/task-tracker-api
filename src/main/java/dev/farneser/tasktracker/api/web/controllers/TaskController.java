package dev.farneser.tasktracker.api.web.controllers;


import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.operations.views.TaskView;
import dev.farneser.tasktracker.api.service.TaskService;
import dev.farneser.tasktracker.api.web.dto.task.CreateTaskDto;
import dev.farneser.tasktracker.api.web.dto.task.PatchTaskDto;
import dev.farneser.tasktracker.api.web.models.Message;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned a list of tasks"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<TaskView>> get(Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(taskService.get(authentication));
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created task"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<TaskView> create(@RequestBody @Valid CreateTaskDto createTaskDto, Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(taskService.create(createTaskDto, authentication));
    }

    @GetMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned a task by id"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<TaskView> getById(@PathVariable Long id, Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(taskService.get(id, authentication));
    }

    @PatchMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted a task by id"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<TaskView> patchById(@PathVariable Long id, @RequestBody @Valid PatchTaskDto patchTaskDto, Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(taskService.patch(id, patchTaskDto, authentication));
    }

    @DeleteMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted a task by id"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Message> deleteById(@PathVariable Long id, Authentication authentication) throws NotFoundException {
        taskService.delete(id, authentication);

        return ResponseEntity.ok(Message.body("Successfully deleted column"));
    }
}
