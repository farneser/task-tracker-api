package dev.farneser.tasktracker.api.web.controllers;


import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.operations.views.ProjectView;
import dev.farneser.tasktracker.api.operations.views.StatusView;
import dev.farneser.tasktracker.api.service.ProjectService;
import dev.farneser.tasktracker.api.service.StatusService;
import dev.farneser.tasktracker.api.web.dto.project.CreateProjectDto;
import dev.farneser.tasktracker.api.web.dto.project.PatchProjectDto;
import dev.farneser.tasktracker.api.web.dto.status.CreateStatusDto;
import dev.farneser.tasktracker.api.web.dto.status.PatchStatusDto;
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
@RequestMapping(EndpointConstants.PROJECT_ENDPOINT)
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final StatusService statusService;

    @GetMapping
    public ResponseEntity<List<ProjectView>> get(
            Authentication authentication
    ) throws NotFoundException {
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
    ) throws NotFoundException {
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

    @PostMapping("{id}/status")
    @Operation(summary = "Create status", description = "Create status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created status"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
    })
    public ResponseEntity<StatusView> createStatus(
            @PathVariable Long id,
            @RequestBody @Valid CreateStatusDto createStatusDto,
            Authentication authentication) throws NotFoundException, OperationNotAuthorizedException {
        log.info("Creating column for user {}, column name: {}", authentication.getName(), createStatusDto.getStatusName());

        return ResponseEntity.status(201).body(statusService.create(id, createStatusDto, authentication));
    }

    @PatchMapping("{id}/status/{statusId}")
    @Operation(summary = "Patch column", description = "Patch column data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully patched column"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Column not found")
    })
    public ResponseEntity<StatusView> patchStatusById(
            @PathVariable Long id,
            @PathVariable Long statusId,
            @RequestBody @Valid PatchStatusDto patchStatusDto,
            Authentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        log.info("Patching column for user {}, column id: {}", authentication.getName(), id);

        return ResponseEntity.ok(statusService.patch(id, statusId, patchStatusDto, authentication));
    }

    @DeleteMapping("{id}/status/{statusId}")
    @Operation(summary = "Delete column", description = "Delete column by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted column"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Column not found")
    })
    public ResponseEntity<Message> deleteStatusById(
            @PathVariable Long id,
            @PathVariable Long statusId,
            Authentication authentication) throws NotFoundException, OperationNotAuthorizedException {
        log.info("Deleting column for user {}, column id: {}", authentication.getName(), id);

        statusService.delete(id, statusId, authentication);

        return ResponseEntity.ok(Message.body("Successfully deleted column"));
    }

}
