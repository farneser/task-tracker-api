package dev.farneser.tasktracker.api.service.messages;

import com.google.gson.Gson;
import dev.farneser.tasktracker.api.models.tokens.ConfirmEmailToken;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * The `MessageService` class provides functionality to send messages related to user registration and confirmation.
 * It uses RabbitMQ to send messages to different queues for processing.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageService {
    private final RabbitTemplate rabbitTemplate;

    /**
     * Sends a registration message for the specified email.
     *
     * @param email The email address for registration.
     */
    public void sendRegisterMessage(@Email String email) {
        log.debug("Sending register message for email {}", email);

        this.sendRegisterMessage(new ConfirmEmailToken(email, null));
    }

    /**
     * Sends a confirmation email message for the provided confirmation token.
     *
     * @param confirmationToken The confirmation token containing email information.
     */
    public void sendConfirmEmail(ConfirmEmailToken confirmationToken) {
        log.debug("Sending confirm email message for email {}", confirmationToken.getEmail());

        rabbitTemplate.convertAndSend(QueueType.CONFIRM_EMAIL.toString(), new Gson().toJson(confirmationToken));
    }

    /**
     * Sends a registration message for the provided confirmation token.
     *
     * @param confirmationToken The confirmation token containing email information.
     */
    public void sendRegisterMessage(ConfirmEmailToken confirmationToken) {
        log.debug("Sending register message for email {}", confirmationToken.getEmail());

        rabbitTemplate.convertAndSend(QueueType.NEW_REGISTER.toString(), new Gson().toJson(confirmationToken));
    }
}
