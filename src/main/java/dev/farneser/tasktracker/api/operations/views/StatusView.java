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
@Schema(name = "StatusView", description = "Status view")
public class StatusView implements ITypeMapper {
    @Schema(name = "id", description = "Status id", example = "1")
    private Long id;
    @Schema(name = "statusName", description = "Status name", example = "To do")
    private String statusView;
    @Schema(name = "isCompleted", description = "Is status completed", example = "false")
    private Boolean isCompleted;
    @Schema(name = "orderNumber", description = "Status order number", example = "1")
    private Long orderNumber;
    @Schema(name = "tasks", description = "Status tasks")
    private List<TaskLookupView> tasks;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(name = "creationDate", description = "Status creation date", example = "2021-01-01T00:00:00.000Z")
    private Date creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(name = "editDate", description = "Status edit date", example = "2021-01-01T00:00:00.000Z")
    private Date editDate;

    public void mapping(ModelMapper modelMapper) {
        log.debug("Mapping StatusView");

        modelMapper.createTypeMap(Status.class, StatusView.class)
                .addMapping(Status::getId, StatusView::setId)
                .addMapping(Status::getStatusName, StatusView::setStatusView)
                .addMapping(Status::getIsCompleted, StatusView::setIsCompleted)
                .addMapping(Status::getIsCompleted, StatusView::setIsCompleted)
                .addMapping(Status::getCreationDate, StatusView::setCreationDate)
                .addMapping(Status::getEditDate, StatusView::setEditDate)
                .addMapping(col -> col.getTasks().stream().map(source -> modelMapper.map(source, TaskView.class)).toList(), StatusView::setTasks);
    }
}
