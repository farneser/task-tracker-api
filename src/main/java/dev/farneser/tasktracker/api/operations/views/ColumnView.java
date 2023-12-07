package dev.farneser.tasktracker.api.operations.views;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.models.KanbanColumn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.List;

@Data
@Slf4j
@Schema(name = "ColumnView", description = "Column view")
public class ColumnView implements ITypeMapper {
    @Schema(name = "id", description = "Column id", example = "1")
    private Long id;
    @Schema(name = "columnName", description = "Column name", example = "To do")
    private String columnName;
    @Schema(name = "isCompleted", description = "Is column completed", example = "false")
    private Boolean isCompleted;
    @Schema(name = "orderNumber", description = "Column order number", example = "1")
    private Long orderNumber;
    @Schema(name = "tasks", description = "Column tasks")
    private List<TaskView> tasks;
    @Schema(name = "creationDate", description = "Column creation date", example = "2021-01-01T00:00:00.000Z")
    private Date creationDate;
    @Schema(name = "editDate", description = "Column edit date", example = "2021-01-01T00:00:00.000Z")
    private Date editDate;

    public void mapping(ModelMapper modelMapper) {
        log.debug("Mapping ColumnView");

        modelMapper.createTypeMap(KanbanColumn.class, ColumnView.class)
                .addMapping(KanbanColumn::getId, ColumnView::setId)
                .addMapping(KanbanColumn::getColumnName, ColumnView::setColumnName)
                .addMapping(KanbanColumn::getIsCompleted, ColumnView::setIsCompleted)
                .addMapping(KanbanColumn::getIsCompleted, ColumnView::setIsCompleted)
                .addMapping(KanbanColumn::getEditDate, ColumnView::setEditDate)
                .addMapping(KanbanColumn::getCreatiionDate, ColumnView::setCreationDate)
                .addMapping(col -> col.getTasks().stream().map(source -> modelMapper.map(source, TaskView.class)).toList(), ColumnView::setTasks);
    }
}
