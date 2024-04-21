package dev.farneser.tasktracker.api.web.controllers;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.operations.views.ProjectInviteTokenView;
import dev.farneser.tasktracker.api.service.ProjectInviteTokenService;
import dev.farneser.tasktracker.api.service.auth.UserAuthentication;
import dev.farneser.tasktracker.api.web.miscellaneous.AuthModel;
import dev.farneser.tasktracker.api.web.models.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(EndpointConstants.PROJECT_ENDPOINT)
@RequiredArgsConstructor
public class ProjectInvitesController {
    private final ProjectInviteTokenService projectInviteTokenService;

    @PostMapping("accept-invite/{token}")
    public ResponseEntity<Message> acceptInvite(
            @PathVariable String token,
            @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        projectInviteTokenService.accept(token, authentication);

        return ResponseEntity.ok(Message.body("Invite successfully accepted"));
    }

    @GetMapping("{id}/invite-token")
    public ResponseEntity<ProjectInviteTokenView> getInvite(
            @PathVariable Long id,
            @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        return ResponseEntity.ok(projectInviteTokenService.get(id, authentication));
    }

    @PostMapping("{id}/invite-token")
    public ResponseEntity<ProjectInviteTokenView> createInvite(
            @PathVariable Long id,
            @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        return ResponseEntity.status(201).body(projectInviteTokenService.create(id, authentication));
    }

    @DeleteMapping("{id}/invite-token")
    public ResponseEntity<Message> deleteInvite(
            @PathVariable Long id,
            @ModelAttribute(AuthModel.NAME) UserAuthentication authentication
    ) throws NotFoundException, OperationNotAuthorizedException {
        projectInviteTokenService.delete(id, authentication);

        return ResponseEntity.ok(Message.body("Successfully deleted invite token"));
    }
}
