package dev.farneser.tasktracker.api.web.controllers;


import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.operations.view.KanbanColumnView;
import dev.farneser.tasktracker.api.service.KanbanColumnService;
import dev.farneser.tasktracker.api.web.dto.KanbanColumnDto;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/column")
@RequiredArgsConstructor
public class KanbanColumnController {
    private final KanbanColumnService columnService;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created column"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    public ResponseEntity<KanbanColumnView> get(@RequestBody @Valid KanbanColumnDto kanbanColumnDto, Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(columnService.create(kanbanColumnDto, authentication));
    }
}
