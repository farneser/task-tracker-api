package dev.farneser.tasktracker.api.service.messages;

import com.google.gson.Gson;
import dev.farneser.tasktracker.api.models.ConfirmEmailToken;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageService {
    private final RabbitTemplate rabbitTemplate;

    public void sendRegisterMessage(@Email String email) {
        this.sendRegisterMessage(new ConfirmEmailToken(email, null));
    }

    public void sendConfirmEmail(ConfirmEmailToken confirmationToken) {
        System.out.println("Sending confirm email " + confirmationToken);
        rabbitTemplate.convertAndSend(QueueType.CONFIRM_EMAIL.toString(), new Gson().toJson(confirmationToken));
    }

    public void sendRegisterMessage(ConfirmEmailToken confirmationToken) {
        rabbitTemplate.convertAndSend(QueueType.NEW_REGISTER.toString(), new Gson().toJson(confirmationToken));
    }
}
