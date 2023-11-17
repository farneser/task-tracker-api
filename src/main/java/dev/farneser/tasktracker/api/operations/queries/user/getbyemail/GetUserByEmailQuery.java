package dev.farneser.tasktracker.api.operations.queries.user.getbyemail;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.view.UserView;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetUserByEmailQuery implements Query<UserView> {
    private String email;
}
