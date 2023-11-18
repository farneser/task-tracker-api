package dev.farneser.tasktracker.api.operations.views;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.models.KanbanColumn;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class KanbanColumnView implements ITypeMapper {
    private Long id;
    private String columnName;
    private Boolean isCompleted;
    private Long orderNumber;

    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(KanbanColumn.class, KanbanColumnView.class)
                .addMapping(KanbanColumn::getId, KanbanColumnView::setId)
                .addMapping(KanbanColumn::getColumnName, KanbanColumnView::setColumnName)
                .addMapping(KanbanColumn::getIsCompleted, KanbanColumnView::setIsCompleted)
                .addMapping(KanbanColumn::getOrderNumber, KanbanColumnView::setOrderNumber);
    }
}
