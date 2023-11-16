package dev.farneser.tasktracker.api.operations.queries.refreshtoken.getbyuserid;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.models.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetRefreshTokenByUserIdQuery implements Query<RefreshToken> {
    private Long id;
}
