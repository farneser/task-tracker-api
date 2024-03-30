package dev.farneser.tasktracker.api.web.dto.status;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.status.patch.PatchStatusCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

@Data
@Slf4j
@Schema(name = "PatchColumnDto", description = "Patch column DTO")
public class PatchStatusDto implements ITypeMapper {
    @Length(min = 1, max = 255)
    @Schema(name = "columnName", description = "Column name", example = "To do")
    private String columnName;
    @Schema(name = "isCompleted", description = "Is column completed", example = "false")
    private Boolean isCompleted;
    @Schema(name = "orderNumber", description = "Column order number", example = "1")
    private Long orderNumber;

    @Override
    public void mapping(ModelMapper modelMapper) {
        log.debug("Mapping PatchColumnDto");

        modelMapper.createTypeMap(PatchStatusDto.class, PatchStatusCommand.class)
                .addMapping(PatchStatusDto::getColumnName, PatchStatusCommand::setStatusName)
                .addMapping(PatchStatusDto::getIsCompleted, PatchStatusCommand::setIsCompleted)
                .addMapping(PatchStatusDto::getOrderNumber, PatchStatusCommand::setOrderNumber);
    }
}
