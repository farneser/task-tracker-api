package dev.farneser.tasktracker.api.operations.queries.column.getbyid;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.ColumnView;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetColumnByIdQuery implements Query<ColumnView> {
    private Long userId;
    private Long columnId;
}
