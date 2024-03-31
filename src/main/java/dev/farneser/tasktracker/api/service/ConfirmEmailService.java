package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.NotFoundException;
import dev.farneser.tasktracker.api.exceptions.OperationNotAuthorizedException;
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

/**
 * The `ConfirmEmailService` class provides functionality for email confirmation and user activation.
 * It manages the creation, sending, and confirmation of email tokens.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmEmailService {
    private final MessageService messageService;
    private final ConfirmEmailTokenRepository confirmEmailTokenRepository;
    private final Mediator mediator;

    @Value("${application.email.confirmation.required:true}")
    private Boolean confirmationRequired;

    // Default token lifetime: 24 hours
    @Value("${application.email.confirmation.token-lifetime:86400000}")
    private Long confirmationTokenLifetime;

    /**
     * Creates a confirmation token for the specified email address.
     *
     * @param email The email address for which to create the confirmation token.
     * @return The created confirmation token.
     */
    private ConfirmEmailToken createConfirmationToken(String email) {
        Date confirmTokenExpiration = new Date(System.currentTimeMillis() + confirmationTokenLifetime);

        log.debug("Confirm token expiration: {}", confirmTokenExpiration);

        return new ConfirmEmailToken(email, confirmTokenExpiration);
    }

    /**
     * Sends a confirmation email or activates the user immediately based on the application's configuration.
     *
     * @param email The email address for which to send the confirmation.
     * @throws NotFoundException If the user is not found.
     */
    public void requireConfirm(String email) throws NotFoundException, OperationNotAuthorizedException {
        ConfirmEmailToken confirmationToken = createConfirmationToken(email);

        if (confirmationRequired) {
            log.debug("Confirm token: {}", confirmationToken);

            confirmEmailTokenRepository.save(confirmationToken, confirmationTokenLifetime);

            log.debug("Confirm token saved: {}", confirmationToken);

            messageService.sendConfirmEmail(confirmationToken);
        } else {
            log.debug("Confirmation not required for email {}", email);

            mediator.send(new ActivateUserCommand(email));

            log.debug("User activated: {}", email);
        }
    }

    /**
     * Sends a registration message and initiates email confirmation or user activation based on the application's configuration.
     *
     * @param email The email address for which to send the registration message.
     * @throws NotFoundException If the user is not found.
     */
    public void sendRegisterMessage(String email) throws NotFoundException, OperationNotAuthorizedException {
        log.debug("Sending register message for email {}", email);

        if (confirmationRequired) {
            log.debug("Confirmation required for email {}", email);

            ConfirmEmailToken confirmationToken = createConfirmationToken(email);

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

    /**
     * Confirms the user's email address using the provided confirmation token.
     *
     * @param id The ID of the confirmation token to use for email confirmation.
     * @throws NotFoundException If the confirmation token is not found.
     */
    public void confirm(UUID id) throws NotFoundException, OperationNotAuthorizedException {
        ConfirmEmailToken confirmEmailToken = confirmEmailTokenRepository.get(id);

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