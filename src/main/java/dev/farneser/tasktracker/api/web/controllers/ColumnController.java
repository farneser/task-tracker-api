package dev.farneser.tasktracker.api.web.controllers;


import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.operations.views.ColumnView;
import dev.farneser.tasktracker.api.operations.views.TaskView;
import dev.farneser.tasktracker.api.service.ColumnService;
import dev.farneser.tasktracker.api.web.dto.column.CreateColumnDto;
import dev.farneser.tasktracker.api.web.dto.column.PatchColumnDto;
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
@RequestMapping("/api/v1/column")
@RequiredArgsConstructor
public class ColumnController {
    private final ColumnService columnService;

    @GetMapping
    @Operation(summary = "Get columns", description = "Get columns by JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got columns"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Columns not found")
    })
    public ResponseEntity<List<ColumnView>> get(Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(columnService.get(authentication));
    }

    @PostMapping
    @Operation(summary = "Create column", description = "Create column")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created column"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
    })
    public ResponseEntity<ColumnView> create(
            @RequestBody @Valid CreateColumnDto createColumnDto,
            Authentication authentication
    ) throws NotFoundException {
        return ResponseEntity.status(201).body(columnService.create(createColumnDto, authentication));
    }

    @GetMapping("{id}")
    @Operation(summary = "Get column", description = "Get column by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got column"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Column not found")
    })
    public ResponseEntity<ColumnView> getById(
            @PathVariable Long id,
            Authentication authentication
    ) throws NotFoundException {
        return ResponseEntity.ok(columnService.get(id, authentication));
    }

    @GetMapping("{id}/tasks")
    @Operation(summary = "Get tasks", description = "Get tasks by column id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got tasks"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Tasks not found")
    })
    public ResponseEntity<List<TaskView>> getTasksById(
            @PathVariable Long id,
            Authentication authentication
    ) throws NotFoundException {
        return ResponseEntity.ok(columnService.getTasks(id, authentication));
    }

    @PatchMapping("{id}")
    @Operation(summary = "Patch column", description = "Patch column data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully patched column"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Column not found")
    })
    public ResponseEntity<ColumnView> patchById(
            @PathVariable Long id,
            @RequestBody @Valid PatchColumnDto patchColumnDto,
            Authentication authentication
    ) throws NotFoundException {
        return ResponseEntity.ok(columnService.patch(id, patchColumnDto, authentication));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete column", description = "Delete column by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted column"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "Column not found")
    })
    public ResponseEntity<Message> deleteById(
            @PathVariable Long id,
            Authentication authentication
    ) throws NotFoundException {
        columnService.delete(id, authentication);

        return ResponseEntity.ok(Message.body("Successfully deleted column"));
    }
}
