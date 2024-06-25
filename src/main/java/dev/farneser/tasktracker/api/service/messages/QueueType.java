package dev.farneser.tasktracker.api.service.messages;

import lombok.Getter;

@Getter
public enum QueueType {
    NEW_REGISTER("new_register"),
    CONFIRM_EMAIL("confirm_email"),
    GET_STATISTICS("get_statistics");

    private final String queueName;

    QueueType(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public String toString() {
        return queueName;
    }
}
