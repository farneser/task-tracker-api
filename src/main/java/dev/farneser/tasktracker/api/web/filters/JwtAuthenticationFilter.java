package dev.farneser.tasktracker.api.web.filters;

import dev.farneser.tasktracker.api.exceptions.InvalidTokenException;
import dev.farneser.tasktracker.api.exceptions.TokenExpiredException;
import dev.farneser.tasktracker.api.service.JwtService;
import dev.farneser.tasktracker.api.service.UserService;
import dev.farneser.tasktracker.api.web.ApiStatus;
import dev.farneser.tasktracker.api.web.models.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTH_PREFIX = "Bearer ";
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException, ServletException {

        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(AUTH_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        var jwt = authHeader.substring(AUTH_PREFIX.length());

        try {
            var email = jwtService.extractUsername(jwt);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var user = this.userService.loadUserByUsername(email);

                if (jwtService.isTokenValid(jwt, user.getUsername())) {
                    var authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

                filterChain.doFilter(request, response);
            }
        } catch (TokenExpiredException e) {
            log.error(e.getMessage());

            handleError(response, e.getMessage(), ApiStatus.ACCESS_TOKEN_EXPIRED.getCode());
        } catch (InvalidTokenException e) {
            log.error(e.getMessage());

            handleError(response, e.getMessage(), ApiStatus.UNAUTHORIZED.getCode());
        }
    }

    private void handleError(HttpServletResponse response, String message, Integer code) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(
                ErrorResponse
                        .builder()
                        .message(message)
                        .status(code)
                        .build()
                        .toString()
        );
    }

}
