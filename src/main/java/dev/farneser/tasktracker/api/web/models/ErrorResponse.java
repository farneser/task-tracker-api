package dev.farneser.tasktracker.api.web.models;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private int status;
    private String message;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
