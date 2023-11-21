package dev.farneser.tasktracker.api.web.dto.column;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.column.patch.PatchColumnCommand;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class PatchColumnDto implements ITypeMapper {
    private String columnName;
    private Boolean isCompleted;
    private Long orderNumber;

    @Override
    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(PatchColumnDto.class, PatchColumnCommand.class)
                .addMapping(PatchColumnDto::getColumnName, PatchColumnCommand::setColumnName)
                .addMapping(PatchColumnDto::getIsCompleted, PatchColumnCommand::setIsCompleted)
                .addMapping(PatchColumnDto::getOrderNumber, PatchColumnCommand::setOrderNumber);
    }
}
