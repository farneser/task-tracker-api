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
@Schema(name = "PatchStatusDto", description = "Patch status DTO")
public class PatchStatusDto implements ITypeMapper {
    @Length(min = 1, max = 255)
    @Schema(name = "statusName", description = "Status name", example = "To do")
    private String statusName;
    @Length(min = 6, max = 6)
    @Schema(name = "statusColor", description = "Status color in hex format", example = "ff00ff")
    private String statusColor;
    @Schema(name = "isCompleted", description = "Is status completed", example = "false")
    private Boolean isCompleted;
    @Schema(name = "orderNumber", description = "Status order number", example = "1")
    private Long orderNumber;

    @Override
    public void mapping(ModelMapper modelMapper) {
        log.debug("Mapping PatchStatusDto");

        modelMapper.createTypeMap(PatchStatusDto.class, PatchStatusCommand.class)
                .addMapping(PatchStatusDto::getStatusName, PatchStatusCommand::setStatusName)
                .addMapping(PatchStatusDto::getStatusColor, PatchStatusCommand::setStatusColor)
                .addMapping(PatchStatusDto::getIsCompleted, PatchStatusCommand::setIsCompleted)
                .addMapping(PatchStatusDto::getOrderNumber, PatchStatusCommand::setOrderNumber);
    }
}
