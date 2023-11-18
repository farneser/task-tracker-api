package dev.farneser.tasktracker.api.web.controllers;


import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.operations.views.KanbanColumnView;
import dev.farneser.tasktracker.api.service.KanbanColumnService;
import dev.farneser.tasktracker.api.web.dto.KanbanColumnDto;
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
@RequestMapping("/api/v1/column")
@RequiredArgsConstructor
public class KanbanColumnController {
    private final KanbanColumnService columnService;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned a list of columns"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
    })
    public ResponseEntity<List<KanbanColumnView>> get(Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(columnService.get(authentication));
    }

    @PostMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned a column by id"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
    })
    public ResponseEntity<KanbanColumnView> getById(@PathVariable Long id, Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(columnService.get(id, authentication));
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created column"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    public ResponseEntity<KanbanColumnView> create(@RequestBody @Valid KanbanColumnDto kanbanColumnDto, Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(columnService.create(kanbanColumnDto, authentication));
    }
}
