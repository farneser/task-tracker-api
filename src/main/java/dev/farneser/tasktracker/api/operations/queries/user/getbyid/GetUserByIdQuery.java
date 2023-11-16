package dev.farneser.tasktracker.api.operations.queries.user.getbyid;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.operations.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetUserByIdQuery implements Query<UserDto> {
    private Long id;
}
