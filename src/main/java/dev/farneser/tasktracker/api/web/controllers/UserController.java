package dev.farneser.tasktracker.api.web.controllers;


import dev.farneser.tasktracker.api.service.UserService;
import dev.farneser.tasktracker.api.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDto> get(Authentication authentication) {
        return ResponseEntity.ok(userService.getUser(authentication));
    }
}
