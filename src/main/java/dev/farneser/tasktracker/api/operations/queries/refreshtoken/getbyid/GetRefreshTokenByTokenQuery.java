package dev.farneser.tasktracker.api.operations.queries.refreshtoken.getbyid;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.models.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetRefreshTokenByTokenQuery implements Query<RefreshToken> {
    private String token;
}
