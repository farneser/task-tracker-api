package dev.farneser.tasktracker.api.web.dto.user;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.user.patch.PatchUserCommand;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class PatchUserDto implements ITypeMapper {
    private Boolean isSubscribed;

    @Override
    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(PatchUserDto.class, PatchUserCommand.class)
                .addMapping(PatchUserDto::getIsSubscribed, PatchUserCommand::setIsSubscribed);
    }
}
