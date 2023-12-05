package dev.farneser.tasktracker.api.web.controllers;


import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.service.UserService;
import dev.farneser.tasktracker.api.web.dto.user.PatchUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserView> get(Authentication authentication) throws NotFoundException {
        return ResponseEntity.ok(userService.getUser(authentication));
    }

    @PatchMapping
    public ResponseEntity<UserView> patchBy(
            @RequestBody @Valid PatchUserDto patchUserDto,
            Authentication authentication
    ) throws NotFoundException {
        return ResponseEntity.ok(userService.patch(patchUserDto, authentication));
    }
}
