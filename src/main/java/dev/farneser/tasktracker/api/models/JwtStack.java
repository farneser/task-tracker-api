package dev.farneser.tasktracker.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtStack {
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiration;
    private Long refreshTokenExpiration;
}
