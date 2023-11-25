package dev.farneser.tasktracker.api.service.messages;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageService {
    private final RabbitTemplate rabbitTemplate;

    public void sendRegisterMessage(@Email String email) {
        rabbitTemplate.convertAndSend(QueueType.NEW_REGISTER.toString(), email);
    }
}
