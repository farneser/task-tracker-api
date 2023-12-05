package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.models.ConfirmEmailToken;
import dev.farneser.tasktracker.api.operations.commands.user.activate.ActivateUserCommand;
import dev.farneser.tasktracker.api.repository.ConfirmEmailTokenRepository;
import dev.farneser.tasktracker.api.service.messages.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmEmailService {
    @Value("${application.email.confirmation.required:true}")
    private Boolean confirmationRequired;

    // default 24h
    @Value("${application.email.confirmation.token-lifetime:86400000}")
    private Long confirmationTokenLifetime;

    private final MessageService messageService;
    private final ConfirmEmailTokenRepository confirmEmailTokenRepository;
    private final Mediator mediator;

    private ConfirmEmailToken createConfirmationToken(String email) {
        var confirmTokenExpiration = new Date(System.currentTimeMillis() + confirmationTokenLifetime);

        return new ConfirmEmailToken(email, confirmTokenExpiration);
    }

    public void requireConfirm(String email) {
        var confirmationToken = createConfirmationToken(email);

        confirmEmailTokenRepository.save(confirmationToken, confirmationTokenLifetime);
        messageService.sendConfirmEmail(confirmationToken);
    }

    public void sendRegisterMessage(String email) throws NotFoundException {
        if (confirmationRequired) {
            var confirmationToken = createConfirmationToken(email);

            confirmEmailTokenRepository.save(confirmationToken, confirmationTokenLifetime);
            messageService.sendRegisterMessage(confirmationToken);
        } else {
            mediator.send(new ActivateUserCommand(email));
            messageService.sendRegisterMessage(email);
        }
    }

    public void confirm(UUID id) throws NotFoundException {
        var confirmEmailToken = confirmEmailTokenRepository.get(id);

        if (confirmEmailToken != null) {
            // FIXME: 12/5/23    send successfully activated account message
            mediator.send(new ActivateUserCommand(confirmEmailToken.getEmail()));
        } else {
            throw new NotFoundException("Confirm email token not found");
        }
    }
}
