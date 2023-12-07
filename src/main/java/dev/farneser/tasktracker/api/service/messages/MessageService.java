package dev.farneser.tasktracker.api.service.messages;

import com.google.gson.Gson;
import dev.farneser.tasktracker.api.models.ConfirmEmailToken;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageService {
    private final RabbitTemplate rabbitTemplate;

    public void sendRegisterMessage(@Email String email) {
        log.debug("Sending register message for email {}", email);

        this.sendRegisterMessage(new ConfirmEmailToken(email, null));
    }

    public void sendConfirmEmail(ConfirmEmailToken confirmationToken) {
        log.debug("Sending confirm email message for email {}", confirmationToken.getEmail());

        rabbitTemplate.convertAndSend(QueueType.CONFIRM_EMAIL.toString(), new Gson().toJson(confirmationToken));
    }

    public void sendRegisterMessage(ConfirmEmailToken confirmationToken) {
        log.debug("Sending register message for email {}", confirmationToken.getEmail());

        rabbitTemplate.convertAndSend(QueueType.NEW_REGISTER.toString(), new Gson().toJson(confirmationToken));
    }
}
