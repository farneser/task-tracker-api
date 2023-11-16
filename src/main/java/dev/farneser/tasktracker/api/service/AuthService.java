package dev.farneser.tasktracker.api.service;

import dev.farneser.tasktracker.api.exceptions.InternalServerException;
import dev.farneser.tasktracker.api.exceptions.InvalidTokenException;
import dev.farneser.tasktracker.api.exceptions.TokenExpiredException;
import dev.farneser.tasktracker.api.exceptions.UniqueDataException;
import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.models.RefreshToken;
import dev.farneser.tasktracker.api.models.User;
import dev.farneser.tasktracker.api.operations.commands.user.register.RegisterUserCommand;
import dev.farneser.tasktracker.api.operations.queries.user.getbyemail.GetUserByEmailQuery;
import dev.farneser.tasktracker.api.operations.queries.user.getbyid.GetUserByIdQuery;
import dev.farneser.tasktracker.api.repository.RefreshTokenRepository;
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
    private final RefreshTokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final Mediator mediator;

    public JwtDto register(@Valid RegisterDto registerDto) throws InternalServerException, UniqueDataException {
        try {
            var command = modelMapper.map(registerDto, RegisterUserCommand.class);

            var userId = mediator.send(command);

            var user = mediator.send(new GetUserByIdQuery(userId));

            return new JwtDto(jwtService.generateAccessToken(user), this.updateRefreshToken(user));

        } catch (DataIntegrityViolationException e) {
            throw new UniqueDataException(registerDto.getEmail() + " already taken");
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public JwtDto authenticate(LoginRequest loginRequest) throws UsernameNotFoundException {
        var user = mediator.send(new GetUserByEmailQuery(loginRequest.getEmail()));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        return new JwtDto(jwtService.generateAccessToken(user), updateRefreshToken(user));
    }

    public JwtDto refresh(JwtDto jwtDto) throws TokenExpiredException, InvalidTokenException {
        var userName = jwtService.extractUsername(jwtDto.getRefreshToken());

        var user = mediator.send(new GetUserByEmailQuery(userName));

        if (!jwtService.isRefreshTokenValid(jwtDto.getRefreshToken(), user)) {
            throw new TokenExpiredException("Invalid token");
        }

        return new JwtDto(jwtService.generateAccessToken(user), updateRefreshToken(user));
    }

    private String updateRefreshToken(User user) {

        var token = tokenRepository.findByUser(user);

        // deletion if available
        token.ifPresent(tokenRepository::delete);

        var newToken = RefreshToken.builder().token(jwtService.generateRefreshToken(user)).user(user).build();

        tokenRepository.save(newToken);

        return newToken.getToken();
    }
}
