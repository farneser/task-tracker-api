package dev.farneser.tasktracker.api.web.controllers.admin;

import dev.farneser.tasktracker.api.models.project.Project;
import dev.farneser.tasktracker.api.operations.views.pageable.Pageable;
import dev.farneser.tasktracker.api.service.admin.AdminProjectService;
import dev.farneser.tasktracker.api.web.controllers.EndpointConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@EnableMethodSecurity
@RequestMapping(EndpointConstants.ADMIN_PROJECT_ENDPOINT)
@RequiredArgsConstructor
public class AdminProjectController {
    private final AdminProjectService projectService;

    @GetMapping
    @PreAuthorize("hasAuthority('admin:get')")
    public ResponseEntity<Pageable<Project>> get(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(projectService.get(page, size));
    }
}
