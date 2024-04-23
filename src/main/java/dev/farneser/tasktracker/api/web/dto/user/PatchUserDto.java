package dev.farneser.tasktracker.api.web.dto.user;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.user.patch.PatchUserCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Schema(name = "PatchUserDto", description = "Patch user DTO")
public class PatchUserDto implements ITypeMapper {
    @Schema(name = "isSubscribed", description = "Is user subscribed for the email notification", example = "false")
    private Boolean isSubscribed;

    @Override
    public void mapping(ModelMapper modelMapper) {
        log.debug("Mapping PatchUserDto");

        modelMapper.createTypeMap(PatchUserDto.class, PatchUserCommand.class)
                .addMapping(PatchUserDto::getIsSubscribed, PatchUserCommand::setIsSubscribed);
    }
}
