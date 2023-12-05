package dev.farneser.tasktracker.api.web.controllers;


import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.operations.views.ColumnView;
import dev.farneser.tasktracker.api.operations.views.TaskView;
import dev.farneser.tasktracker.api.service.ColumnService;
import dev.farneser.tasktracker.api.web.dto.column.CreateColumnDto;
import dev.farneser.tasktracker.api.web.dto.column.PatchColumnDto;
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
@RequestMapping("/api/v1/column")
@RequiredArgsConstructor
public class ColumnController {
    private final ColumnService columnService;

    @GetMapping
    public ResponseEntity<List<ColumnView>> get(Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(columnService.get(authentication));
    }

    @PostMapping
    public ResponseEntity<ColumnView> create(
            @RequestBody @Valid CreateColumnDto createColumnDto,
            Authentication authentication
    ) throws NotFoundException {
        return ResponseEntity.ok(columnService.create(createColumnDto, authentication));
    }

    @GetMapping("{id}")
    public ResponseEntity<ColumnView> getById(
            @PathVariable Long id,
            Authentication authentication
    ) throws NotFoundException {
        return ResponseEntity.ok(columnService.get(id, authentication));
    }

    @GetMapping("{id}/tasks")
    public ResponseEntity<List<TaskView>> getTasksById(
            @PathVariable Long id,
            Authentication authentication
    ) throws NotFoundException {
        return ResponseEntity.ok(columnService.getTasks(id, authentication));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ColumnView> patchById(
            @PathVariable Long id,
            @RequestBody @Valid PatchColumnDto patchColumnDto,
            Authentication authentication
    ) throws NotFoundException {
        return ResponseEntity.ok(columnService.patch(id, patchColumnDto, authentication));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Message> deleteById(
            @PathVariable Long id,
            Authentication authentication
    ) throws NotFoundException {
        columnService.delete(id, authentication);

        return ResponseEntity.ok(Message.body("Successfully deleted column"));
    }
}
