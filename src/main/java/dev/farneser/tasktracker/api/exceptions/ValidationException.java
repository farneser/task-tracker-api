package dev.farneser.tasktracker.api.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationException extends Exception {
    private final List<String> messages = new ArrayList<>();

    public ValidationException(List<String> messages) {
        super("Validation errors: " + String.join(", ", messages));

        this.messages.addAll(messages);
    }
}
