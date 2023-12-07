package dev.farneser.tasktracker.api.web.dto.column;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.column.patch.PatchColumnCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

@Data
@Slf4j
@Schema(name = "PatchColumnDto", description = "Patch column DTO")
public class PatchColumnDto implements ITypeMapper {
    @Schema(name = "columnName", description = "Column name", example = "To do")
    private String columnName;
    @Schema(name = "isCompleted", description = "Is column completed", example = "false")
    private Boolean isCompleted;
    @Schema(name = "orderNumber", description = "Column order number", example = "1")
    private Long orderNumber;

    @Override
    public void mapping(ModelMapper modelMapper) {
        log.debug("Mapping PatchColumnDto");

        modelMapper.createTypeMap(PatchColumnDto.class, PatchColumnCommand.class)
                .addMapping(PatchColumnDto::getColumnName, PatchColumnCommand::setColumnName)
                .addMapping(PatchColumnDto::getIsCompleted, PatchColumnCommand::setIsCompleted)
                .addMapping(PatchColumnDto::getOrderNumber, PatchColumnCommand::setOrderNumber);
    }
}
