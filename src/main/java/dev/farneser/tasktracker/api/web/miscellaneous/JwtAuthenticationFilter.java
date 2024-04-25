package dev.farneser.tasktracker.api.web.miscellaneous;

import dev.farneser.tasktracker.api.exceptions.InvalidTokenException;
import dev.farneser.tasktracker.api.exceptions.TokenExpiredException;
import dev.farneser.tasktracker.api.service.UserService;
import dev.farneser.tasktracker.api.service.auth.JwtService;
import dev.farneser.tasktracker.api.dto.models.ErrorResponse;
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
import org.springframework.security.core.userdetails.UserDetails;
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
        log.debug("Jwt authentication filter");

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        log.debug("Auth header: {}", authHeader);

        // skip if no auth or request for auth
        if (authHeader == null || !authHeader.startsWith(AUTH_PREFIX) || request.getRequestURI().startsWith("/api/v1/auth")) {
            log.debug("Skip jwt authentication filter");

            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(AUTH_PREFIX.length());

        try {
            String username = jwtService.extractUsername(jwt);

            log.debug("Email: {}", username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = this.userService.loadUserByUsername(username);

                log.debug("User: {}", user);

                if (jwtService.isTokenValid(jwt, user.getUsername())) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    log.debug("Auth token created for user: {}", user.getUsername());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    log.debug("Auth token details set for user: {}", user.getUsername());

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

                filterChain.doFilter(request, response);
            }
        } catch (TokenExpiredException e) {
            log.debug(e.getMessage());

            handleError(response, e.getMessage(), ApiStatus.ACCESS_TOKEN_EXPIRED.getCode());
        } catch (InvalidTokenException e) {
            log.debug(e.getMessage());

            handleError(response, e.getMessage(), ApiStatus.UNAUTHORIZED.getCode());
        }
    }

    private void handleError(HttpServletResponse response, String message, Integer code) throws IOException {
        log.debug("Handle error with message: {}, code: {}", message, code);

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
