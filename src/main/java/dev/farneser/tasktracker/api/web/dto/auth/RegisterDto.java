package dev.farneser.tasktracker.api.web.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.user.register.PasswordMatches;
import dev.farneser.tasktracker.api.operations.commands.user.register.RegisterUserCommand;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

@Data
@JsonNaming
@PasswordMatches
public class RegisterDto implements ITypeMapper {
    @NonNull
    @Email(message = "You should enter email like 'example@email.com'")
    private String email;
    @NonNull
    @Length(min = 8, max = 64)
    private String password;
    @Length(min = 8, max = 64)
    @JsonProperty("confirm_password")
    private String confirmPassword;

    @Override
    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(RegisterDto.class, RegisterUserCommand.class)
                .addMapping(RegisterDto::getEmail, RegisterUserCommand::setEmail)
                .addMapping(RegisterDto::getPassword, RegisterUserCommand::setPassword)
                .addMapping(RegisterDto::getConfirmPassword, RegisterUserCommand::setConfirmPassword);

    }
}
