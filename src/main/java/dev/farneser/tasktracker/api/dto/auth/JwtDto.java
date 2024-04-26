package dev.farneser.tasktracker.api.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "JwtDto", description = "JWT token DTO")
public class JwtDto {
    @JsonProperty("accessToken")
    @Schema(name = "accessToken", description = "JWT access token", example = "your_access_token")
    private String accessToken;
    @Schema(name = "refreshToken", description = "JWT refresh token", example = "your_refresh_token")
    @JsonProperty("refreshToken")
    private String refreshToken;
}
