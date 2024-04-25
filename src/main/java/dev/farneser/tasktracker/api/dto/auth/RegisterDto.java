package dev.farneser.tasktracker.api.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.user.register.RegisterUserCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

@Data
@Slf4j
@JsonNaming
@Schema(name = "RegisterDto", description = "Register DTO")
public class RegisterDto implements ITypeMapper {
    @Length(min = 4, max = 64)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    @Schema(name = "username", description = "User name", example = "example_user")
    private String username;
    @Email(message = "You should enter email like 'example@email.com'")
    @Schema(name = "email", description = "User email", example = "example@email.com")
    private String email;
    @Length(min = 8, max = 64)
    @Schema(name = "password", description = "User password", example = "password", minLength = 8, maxLength = 64)
    private String password;
    @Length(min = 8, max = 64)
    @JsonProperty("confirmPassword")
    @Schema(name = "confirmPassword", description = "User password confirmation", example = "password", minLength = 8, maxLength = 64)
    private String confirmPassword;

    @Override
    public void mapping(ModelMapper modelMapper) {
        log.debug("Mapping RegisterDto");

        modelMapper.createTypeMap(RegisterDto.class, RegisterUserCommand.class)
                .addMapping(RegisterDto::getEmail, RegisterUserCommand::setEmail)
                .addMapping(RegisterDto::getPassword, RegisterUserCommand::setPassword)
                .addMapping(RegisterDto::getConfirmPassword, RegisterUserCommand::setConfirmPassword);

    }
}
