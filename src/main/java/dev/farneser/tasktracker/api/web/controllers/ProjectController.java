package dev.farneser.tasktracker.api.web.controllers;


import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.operations.views.ProjectView;
import dev.farneser.tasktracker.api.service.ProjectService;
import dev.farneser.tasktracker.api.web.dto.project.CreateProjectDto;
import dev.farneser.tasktracker.api.web.dto.project.PatchProjectDto;
import dev.farneser.tasktracker.api.web.models.Message;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

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
}
