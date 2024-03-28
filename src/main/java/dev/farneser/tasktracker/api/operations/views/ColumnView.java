package dev.farneser.tasktracker.api.operations.views;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.models.Status;
import dev.farneser.tasktracker.api.operations.views.task.TaskLookupView;
import dev.farneser.tasktracker.api.operations.views.task.TaskView;
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
    private List<TaskLookupView> tasks;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(name = "creationDate", description = "Column creation date", example = "2021-01-01T00:00:00.000Z")
    private Date creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(name = "editDate", description = "Column edit date", example = "2021-01-01T00:00:00.000Z")
    private Date editDate;

    public void mapping(ModelMapper modelMapper) {
        log.debug("Mapping ColumnView");

        modelMapper.createTypeMap(Status.class, ColumnView.class)
                .addMapping(Status::getId, ColumnView::setId)
                .addMapping(Status::getStatusName, ColumnView::setColumnName)
                .addMapping(Status::getIsCompleted, ColumnView::setIsCompleted)
                .addMapping(Status::getIsCompleted, ColumnView::setIsCompleted)
                .addMapping(Status::getCreationDate, ColumnView::setCreationDate)
                .addMapping(Status::getEditDate, ColumnView::setEditDate)
                .addMapping(col -> col.getTasks().stream().map(source -> modelMapper.map(source, TaskView.class)).toList(), ColumnView::setTasks);
    }
}
