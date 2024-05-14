package dev.farneser.tasktracker.api.dto;

import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "Message", description = "Message from server")
public class Message {
    @Schema(name = "status", description = "Status", example = "201")
    private int status;
    @Schema(name = "message", description = "Query result message", example = "Successfully registered user")
    private String message;

    public Message(String message) {
        this.message = message;
        this.status = 200;
    }

    public static Message body(String message) {
        return new Message(message);
    }

    public static Message body(int status, String message) {
        return new Message(status, message);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
