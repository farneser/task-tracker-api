package dev.farneser.tasktracker.api.operations.queries.status.getbyid;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.StatusView;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetStatusByIdQuery implements Query<StatusView> {
    private Long userId;
    private Long statusId;
}
