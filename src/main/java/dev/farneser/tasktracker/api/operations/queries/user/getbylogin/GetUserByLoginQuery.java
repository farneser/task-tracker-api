package dev.farneser.tasktracker.api.operations.queries.user.getbylogin;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.UserView;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetUserByLoginQuery implements Query<UserView> {
    private String login;
}
