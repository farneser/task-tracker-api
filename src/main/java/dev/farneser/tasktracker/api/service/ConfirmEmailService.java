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
    private final MessageService messageService;
    private final ConfirmEmailTokenRepository confirmEmailTokenRepository;
    private final Mediator mediator;
    @Value("${application.email.confirmation.required:true}")
    private Boolean confirmationRequired;
    // default 24h
    @Value("${application.email.confirmation.token-lifetime:86400000}")
    private Long confirmationTokenLifetime;

    private ConfirmEmailToken createConfirmationToken(String email) {
        var confirmTokenExpiration = new Date(System.currentTimeMillis() + confirmationTokenLifetime);

        log.debug("Confirm token expiration: {}", confirmTokenExpiration);

        return new ConfirmEmailToken(email, confirmTokenExpiration);
    }

    public void requireConfirm(String email) {
        var confirmationToken = createConfirmationToken(email);

        log.debug("Confirm token: {}", confirmationToken);

        confirmEmailTokenRepository.save(confirmationToken, confirmationTokenLifetime);

        log.debug("Confirm token saved: {}", confirmationToken);

        messageService.sendConfirmEmail(confirmationToken);
    }

    public void sendRegisterMessage(String email) throws NotFoundException {
        log.debug("Sending register message for email {}", email);

        if (confirmationRequired) {
            log.debug("Confirmation required for email {}", email);

            var confirmationToken = createConfirmationToken(email);

            log.debug("Confirm token: {}", confirmationToken);

            confirmEmailTokenRepository.save(confirmationToken, confirmationTokenLifetime);
            messageService.sendRegisterMessage(confirmationToken);
        } else {
            log.debug("Confirmation not required for email {}", email);

            mediator.send(new ActivateUserCommand(email));

            log.debug("User activated: {}", email);

            messageService.sendRegisterMessage(email);
        }
    }

    public void confirm(UUID id) throws NotFoundException {
        var confirmEmailToken = confirmEmailTokenRepository.get(id);

        log.debug("Confirm email token: {}", confirmEmailToken);

        if (confirmEmailToken != null) {
            log.debug("Confirm email token found: {}", confirmEmailToken);

            // FIXME: 12/5/23    send successfully activated account message
            mediator.send(new ActivateUserCommand(confirmEmailToken.getEmail()));

            log.debug("User activated: {}", confirmEmailToken.getEmail());

            confirmEmailTokenRepository.delete(id);
        } else {
            log.debug("Confirm email token not found: {}", id);

            throw new NotFoundException("Confirm email token not found");
        }
    }
}
