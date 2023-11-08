package dev.farneser.tasktracker.api.models;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private String message;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Message body(String message) {
        return new Message(message);
    }
}
