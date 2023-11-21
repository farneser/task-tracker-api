package dev.farneser.tasktracker.api.operations.queries.column.getbyuserid;

import dev.farneser.tasktracker.api.mediator.Query;
import dev.farneser.tasktracker.api.operations.views.ColumnView;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetColumnByUserIdQuery implements Query<List<ColumnView>> {
    private Long userId;
}
