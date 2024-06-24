package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
import dev.farneser.tasktracker.api.operations.views.UserView;
import dev.farneser.tasktracker.api.service.auth.UserAuthentication;
import dev.farneser.tasktracker.api.service.messages.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstantNotificationService {
    private final MessageService messageService;
    private final UserService userService;

    public void getStatistics(UserAuthentication authentication)
            throws NotFoundException, OperationNotAuthorizedException {
        UserView user = userService.getUser(authentication);

        messageService.getStatistics(user.getId());
    }
}
