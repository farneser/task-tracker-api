package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.*;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.operations.commands.refreshtoken.create.CreateRefreshTokenCommand;
import dev.farneser.tasktracker.api.operations.commands.refreshtoken.deletebyuserid.DeleteRefreshTokenByUserIdCommand;
import dev.farneser.tasktracker.api.operations.commands.user.register.RegisterUserCommand;
import dev.farneser.tasktracker.api.operations.queries.refreshtoken.getbytoken.GetRefreshTokenByIdQuery;
import dev.farneser.tasktracker.api.operations.queries.user.getbyemail.GetUserByEmailQuery;
import dev.farneser.tasktracker.api.operations.queries.user.getbyid.GetUserByIdQuery;
import dev.farneser.tasktracker.api.web.dto.JwtDto;
import dev.farneser.tasktracker.api.web.dto.LoginRequest;
import dev.farneser.tasktracker.api.web.dto.RegisterDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final Mediator mediator;

    public JwtDto register(@Valid RegisterDto registerDto) throws InternalServerException, UniqueDataException {
        try {
            var command = modelMapper.map(registerDto, RegisterUserCommand.class);

            var userId = mediator.send(command);

            var user = mediator.send(new GetUserByIdQuery(userId));

            return new JwtDto(jwtService.generateAccessToken(user.getEmail()), this.updateRefreshToken(user.getEmail()));

        } catch (DataIntegrityViolationException e) {
            throw new UniqueDataException(registerDto.getEmail() + " already taken");
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public JwtDto authenticate(LoginRequest loginRequest) throws UsernameNotFoundException, NotFoundException {
        var user = mediator.send(new GetUserByEmailQuery(loginRequest.getEmail()));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        return new JwtDto(jwtService.generateAccessToken(user.getEmail()), updateRefreshToken(user.getEmail()));
    }

    public JwtDto refresh(JwtDto jwtDto) throws TokenExpiredException, InvalidTokenException, NotFoundException {
        var userName = jwtService.extractUsername(jwtDto.getRefreshToken());

        var user = mediator.send(new GetUserByEmailQuery(userName));

        if (!jwtService.isRefreshTokenValid(jwtDto.getRefreshToken(), user.getEmail())) {
            throw new TokenExpiredException("Invalid token");
        }

        return new JwtDto(jwtService.generateAccessToken(user.getEmail()), updateRefreshToken(user.getEmail()));
    }

    private String updateRefreshToken(String email) throws NotFoundException {

        var user = mediator.send(new GetUserByEmailQuery(email));

        mediator.send(new DeleteRefreshTokenByUserIdCommand(user.getId()));

        var tokenString = jwtService.generateRefreshToken(user.getEmail());

        var tokenId = mediator.send(new CreateRefreshTokenCommand(tokenString, user.getId()));

        var newToken = mediator.send(new GetRefreshTokenByIdQuery(tokenId));

        return newToken.getToken();
    }
}
