package dev.farneser.tasktracker.api.web.controllers;

import dev.farneser.tasktracker.api.dto.models.Message;
import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.exceptions.ValidationException;
import dev.farneser.tasktracker.api.operations.views.ProjectInviteTokenView;
import dev.farneser.tasktracker.api.operations.views.ProjectMemberView;
import dev.farneser.tasktracker.api.service.ProjectInviteTokenService;
import dev.farneser.tasktracker.api.service.auth.UserAuthentication;
import dev.farneser.tasktracker.api.web.miscellaneous.AuthModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(EndpointConstants.PROJECT_ENDPOINT)
@RequiredArgsConstructor
public class ProjectInvitesController {
    private final ProjectInviteTokenService projectInviteTokenService;

    @GetMapping("accept-invite/{token}")
    public ResponseEntity<ProjectInviteTokenView> getAcceptInvite(
            @PathVariable String token
    ) throws NotFoundException, OperationNotAuthorizedException {
        return ResponseEntity.ok(projectInviteTokenService.getAcceptToken(token));
    }

    @PostMapping("accept-invite/{token}")
    public ResponseEntity<ProjectMemberView> acceptInvite(
            @PathVariable String token,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        return ResponseEntity.ok(projectInviteTokenService.accept(token, authentication));
    }

    @GetMapping("{id}/invite-token")
    public ResponseEntity<ProjectInviteTokenView> getInvite(
            @PathVariable Long id,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        return ResponseEntity.ok(projectInviteTokenService.get(id, authentication));
    }

    @PostMapping("{id}/invite-token")
    public ResponseEntity<ProjectInviteTokenView> createInvite(
            @PathVariable Long id,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        return ResponseEntity.status(201).body(projectInviteTokenService.create(id, authentication));
    }

    @DeleteMapping("{id}/invite-token")
    public ResponseEntity<Message> deleteInvite(
            @PathVariable Long id,
            @Schema(hidden = true) @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException, ValidationException {
        projectInviteTokenService.delete(id, authentication);

        return ResponseEntity.ok(Message.body("Successfully deleted invite token"));
    }
}
