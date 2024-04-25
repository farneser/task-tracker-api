package dev.farneser.tasktracker.api.dto.models;

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
@Schema(name = "ErrorResponse", description = "Error response DTO")
public class ErrorResponse {
    @Schema(name = "status", description = "Error status", example = "400")
    private int status;
    @Schema(name = "message", description = "Error message", example = "Bad request")
    private String message;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
