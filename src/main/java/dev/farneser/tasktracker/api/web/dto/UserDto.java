package dev.farneser.tasktracker.api.web.dto;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserDto implements ITypeMapper {
    private long id;
    private String email;

    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(User.class, UserDto.class)
                .addMapping(User::getId, UserDto::setId)
                .addMapping(User::getEmail, UserDto::setEmail);

    }
}
