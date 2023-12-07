package dev.farneser.tasktracker.api.service.auth;

import dev.farneser.tasktracker.api.exceptions.*;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.operations.commands.refreshtoken.create.CreateRefreshTokenCommand;
import dev.farneser.tasktracker.api.operations.commands.user.register.RegisterUserCommand;
import dev.farneser.tasktracker.api.operations.queries.refreshtoken.getbyid.GetRefreshTokenByTokenQuery;
import dev.farneser.tasktracker.api.operations.queries.user.getbyemail.GetUserByEmailQuery;
import dev.farneser.tasktracker.api.operations.queries.user.getbyid.GetUserByIdQuery;
import dev.farneser.tasktracker.api.service.BaseService;
import dev.farneser.tasktracker.api.service.ConfirmEmailService;
import dev.farneser.tasktracker.api.web.dto.auth.JwtDto;
import dev.farneser.tasktracker.api.web.dto.auth.LoginRequest;
import dev.farneser.tasktracker.api.web.dto.auth.RegisterDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class AuthService extends BaseService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ConfirmEmailService confirmEmailService;

    @Autowired
    public AuthService(Mediator mediator, ModelMapper modelMapper, JwtService jwtService, AuthenticationManager authenticationManager, ConfirmEmailService confirmEmailService) {
        super(mediator, modelMapper);
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.confirmEmailService = confirmEmailService;
    }

    public JwtDto register(@Valid RegisterDto registerDto) throws InternalServerException, UniqueDataException {
        try {
            var command = modelMapper.map(registerDto, RegisterUserCommand.class);

            mediator.send(command);

            confirmEmailService.sendRegisterMessage(registerDto.getEmail());

            return authenticate(new LoginRequest(registerDto.getEmail(), registerDto.getPassword()));
        } catch (DataIntegrityViolationException e) {
            throw new UniqueDataException(registerDto.getEmail() + " already taken");
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public JwtDto authenticate(LoginRequest loginRequest) throws UsernameNotFoundException, NotFoundException {
        var user = mediator.send(new GetUserByEmailQuery(loginRequest.getEmail()));

        if (!user.isEnabled()) {
            confirmEmailService.requireConfirm(user.getEmail());
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        return new JwtDto(jwtService.generateAccessToken(user.getEmail()), updateRefreshToken(user.getEmail()));
    }

    public JwtDto refresh(JwtDto jwtDto) throws TokenExpiredException, InvalidTokenException, NotFoundException {
        var refreshToken = mediator.send(new GetRefreshTokenByTokenQuery(jwtDto.getRefreshToken()));

        var user = refreshToken.getUser();

        if (!jwtService.isRefreshTokenValid(jwtDto.getRefreshToken(), user.getEmail())) {
            throw new TokenExpiredException("Invalid token");
        }

        return new JwtDto(jwtService.generateAccessToken(user.getEmail()), updateRefreshToken(user.getEmail()));
    }

    private String updateRefreshToken(String email) throws NotFoundException {

        var user = mediator.send(new GetUserByEmailQuery(email));

        var tokenString = jwtService.generateRefreshToken(user.getEmail());

        mediator.send(new CreateRefreshTokenCommand(tokenString, user.getId()));

        return tokenString;
    }

    public void activateAccount(UUID id) throws NotFoundException {
        confirmEmailService.confirm(id);
    }
}
