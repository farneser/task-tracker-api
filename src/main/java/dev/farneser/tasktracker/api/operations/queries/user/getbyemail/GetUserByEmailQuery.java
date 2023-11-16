package dev.farneser.tasktracker.api.operations.queries.user.getbyemail;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.operations.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetUserByEmailQuery implements Query<UserDto> {
    private String email;
}
