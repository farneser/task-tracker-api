package dev.farneser.tasktracker.api.web.controllers;


import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.operations.views.StatusView;
import dev.farneser.tasktracker.api.operations.views.task.TaskLookupView;
import dev.farneser.tasktracker.api.service.StatusService;
import dev.farneser.tasktracker.api.web.dto.status.CreateStatusDto;
import dev.farneser.tasktracker.api.web.dto.status.PatchStatusDto;
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
@RequestMapping(EndpointConstants.STATUS_ENDPOINT)
@RequiredArgsConstructor
public class StatusController {
    private final StatusService statusService;

    @GetMapping
    @Operation(summary = "Get statuses", description = "Get statuses by JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got statuses"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Statuses not found")
    })
    public ResponseEntity<List<StatusView>> get(
            @Parameter(description = "Toggles inclusion of current task details in the response")
            @RequestParam(defaultValue = "true") Boolean retrieveTasks,
            Authentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        log.info("Getting statuses for user {}", authentication.getName());

        return ResponseEntity.ok(statusService.get(retrieveTasks, authentication));
    }

    @GetMapping("{id}")
    @Operation(summary = "Get status", description = "Get status by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got status"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Status not found")
    })
    public ResponseEntity<StatusView> getById(
            @PathVariable Long id,
            Authentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        log.info("Getting status for user {}, status id: {}", authentication.getName(), id);

        return ResponseEntity.ok(statusService.get(id, authentication));
    }

    @PostMapping
    @Operation(summary = "Create status", description = "Create status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created status"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
    })
    public ResponseEntity<StatusView> create(
            @RequestBody @Valid CreateStatusDto createStatusDto,
            Authentication authentication) throws NotFoundException, OperationNotAuthorizedException {
        log.info("Creating status for user {}, status name: {}", authentication.getName(), createStatusDto.getStatusName());

        return ResponseEntity.status(201).body(statusService.create(createStatusDto, authentication));
    }

    @PatchMapping("{id}")
    @Operation(summary = "Patch status", description = "Patch status data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully patched status"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Status not found")
    })
    public ResponseEntity<StatusView> patchById(
            @PathVariable Long id,
            @RequestBody @Valid PatchStatusDto patchStatusDto,
            Authentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        log.info("Patching status for user {}, status id: {}", authentication.getName(), id);

        return ResponseEntity.ok(statusService.patch(id, patchStatusDto, authentication));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete status", description = "Delete status by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted status"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "status not found")
    })
    public ResponseEntity<Message> deleteById(
            @PathVariable Long id,
            Authentication authentication) throws NotFoundException, OperationNotAuthorizedException {
        log.info("Deleting status for user {}, status id: {}", authentication.getName(), id);

        statusService.delete(id, authentication);

        return ResponseEntity.ok(Message.body("Successfully deleted status"));
    }

    @GetMapping("{id}/tasks")
    @Operation(summary = "Get tasks", description = "Get tasks by status id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got tasks"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Tasks not found")
    })
    public ResponseEntity<List<TaskLookupView>> getTasksById(
            @PathVariable Long id,
            Authentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        log.info("Getting tasks for user {}, status id: {}", authentication.getName(), id);

        return ResponseEntity.ok(statusService.getTasks(id, authentication));
    }
}
