package dev.farneser.tasktracker.api.operations.views;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserView implements ITypeMapper {
    private Long id;
    private String email;
    private Boolean isSubscribed;
    private Date registrationDate;
    private boolean isEnabled;

    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(User.class, UserView.class)
                .addMapping(User::getId, UserView::setId)
                .addMapping(User::getEmail, UserView::setEmail)
                .addMapping(User::getRegisterDate, UserView::setRegistrationDate)
                .addMapping(User::isSubscribed, UserView::setIsSubscribed)
                .addMapping(User::isEnabled, UserView::setEnabled);

    }
}
