package dev.farneser.tasktracker.api.operations.views;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Slf4j
@Component
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UserView", description = "User view")
public class UserView implements ITypeMapper {
    @Schema(name = "id", description = "User id", example = "1")
    private Long id;
    @Schema(name = "email", description = "User email", example = "example@email.com")
    private String email;
    @Schema(name = "isSubscribed", description = "Is user subscribed for the email notification", example = "false")
    private Boolean isSubscribed;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(name = "registrationDate", description = "User registration date", example = "2021-01-01T00:00:00.000Z")
    private Date registrationDate;
    @Schema(name = "isEnabled", description = "Is user enabled", example = "true")
    private boolean isEnabled;

    public void mapping(ModelMapper modelMapper) {
        log.debug("Mapping UserView");

        modelMapper.createTypeMap(User.class, UserView.class)
                .addMapping(User::getId, UserView::setId)
                .addMapping(User::getEmail, UserView::setEmail)
                .addMapping(User::getRegisterDate, UserView::setRegistrationDate)
                .addMapping(User::isSubscribed, UserView::setIsSubscribed)
                .addMapping(User::isEnabled, UserView::setEnabled);

    }
}
