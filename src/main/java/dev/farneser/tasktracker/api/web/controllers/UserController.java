package dev.farneser.tasktracker.api.web.controllers;


import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.service.UserService;
import dev.farneser.tasktracker.api.service.auth.UserAuthentication;
import dev.farneser.tasktracker.api.web.dto.user.PatchUserDto;
import dev.farneser.tasktracker.api.web.miscellaneous.AuthModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(EndpointConstants.USER_ENDPOINT)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get user", description = "Get user by JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got user"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserView> get(
            @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        log.info("Getting user {}", authentication.getName());

        return ResponseEntity.ok(userService.getUser(authentication));
    }

    @PatchMapping
    @Operation(summary = "Patch user", description = "Patch user data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully patched user"),
            @ApiResponse(responseCode = "401", description = "JWT token expired or invalid"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserView> patchBy(
            @RequestBody @Valid PatchUserDto patchUserDto,
            @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        log.info("Patching user {}", authentication.getName());

        return ResponseEntity.ok(userService.patch(patchUserDto, authentication));
    }
}
