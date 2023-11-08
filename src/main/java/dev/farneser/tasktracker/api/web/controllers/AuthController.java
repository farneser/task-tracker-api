package dev.farneser.tasktracker.api.web.controllers;

import dev.farneser.tasktracker.api.web.dto.RegisterDto;
import dev.farneser.tasktracker.api.web.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/user")
    public ResponseEntity<UserDto> register(@RequestBody @Valid RegisterDto registerDto) {
        return ResponseEntity.ok(new UserDto(registerDto.getEmail()));
    }
}
