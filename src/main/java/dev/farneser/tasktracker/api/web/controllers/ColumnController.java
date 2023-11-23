package dev.farneser.tasktracker.api.web.controllers;


import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.operations.views.ColumnView;
import dev.farneser.tasktracker.api.operations.views.TaskView;
import dev.farneser.tasktracker.api.service.ColumnService;
import dev.farneser.tasktracker.api.web.dto.column.CreateColumnDto;
import dev.farneser.tasktracker.api.web.dto.column.PatchColumnDto;
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
@RequestMapping("/api/v1/column")
@RequiredArgsConstructor
public class ColumnController {
    private final ColumnService columnService;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned a list of columns"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ColumnView>> get(Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(columnService.get(authentication));
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created column"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ColumnView> create(@RequestBody @Valid CreateColumnDto createColumnDto, Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(columnService.create(createColumnDto, authentication));
    }

    @GetMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned a column by id"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ColumnView> getById(@PathVariable Long id, Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(columnService.get(id, authentication));
    }

    @GetMapping("{id}/tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned a list of tasks by column by id"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<TaskView>> getTasksById(@PathVariable Long id, Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(columnService.getTasks(id, authentication));
    }

    @PatchMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted a column by id"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ColumnView> patchById(@PathVariable Long id, @RequestBody @Valid PatchColumnDto patchColumnDto, Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(columnService.patch(id, patchColumnDto, authentication));
    }

    @DeleteMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted a column by id"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Message> deleteById(@PathVariable Long id, Authentication authentication) throws NotFoundException {
        columnService.delete(id, authentication);

        return ResponseEntity.ok(Message.body("Successfully deleted column"));
    }
}
