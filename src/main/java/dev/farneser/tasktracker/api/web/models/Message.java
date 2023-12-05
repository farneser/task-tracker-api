package dev.farneser.tasktracker.api.web.models;

import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "Message", description = "Message DTO")
public class Message {
    @Schema(name = "message", description = "Message", example = "Hello world")
    private String message;

    public static Message body(String message) {
        return new Message(message);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
