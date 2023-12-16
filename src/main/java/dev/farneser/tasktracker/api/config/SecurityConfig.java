package dev.farneser.tasktracker.api.config;

import dev.farneser.tasktracker.api.service.UserService;
import dev.farneser.tasktracker.api.web.filters.JwtAuthenticationFilter;
import dev.farneser.tasktracker.api.web.models.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**", "/api/v1/auth", "/error"};
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.debug("Security filter chain");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> {
                    log.debug("Authorize http requests");
                    log.debug("White list url: {}", Arrays.toString(WHITE_LIST_URL));
                    req.requestMatchers(HttpMethod.OPTIONS, WHITE_LIST_URL).permitAll();
                    req.requestMatchers(HttpMethod.GET, WHITE_LIST_URL).permitAll();
                    req.requestMatchers(HttpMethod.POST, WHITE_LIST_URL).permitAll();
                    req.requestMatchers(HttpMethod.PUT, WHITE_LIST_URL).permitAll();
                    req.requestMatchers(HttpMethod.PATCH, WHITE_LIST_URL).permitAll();
                    req.requestMatchers(HttpMethod.DELETE, WHITE_LIST_URL).permitAll();

                    log.debug("Authorize swagger requests");
                    req.requestMatchers(
                            "/",
                            "/swagger-ui.html",
                            "/swagger-ui/**",
                            "/v3/api-docs",
                            "/v3/api-docs/swagger-config"
                    ).permitAll();

                    log.debug("Authorize any request");
                    req.anyRequest().authenticated();
                })
                .formLogin(login -> {
                    log.debug("Form login");
                    login.successHandler(this.successAuth());
                    login.failureHandler(this.failureAuth());
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterAt(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(authEntryPoint()));

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();

        log.debug("Creating authentication provider");

        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);

        log.debug("Authentication provider created");

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    private AuthenticationSuccessHandler successAuth() {
        return (request, response, authentication) -> {
            response.setStatus(200);
            response.getWriter().append(Message.body("OK").toString());
        };
    }

    private AuthenticationFailureHandler failureAuth() {
        return (request, response, e) -> {
            response.setStatus(401);
            response.getWriter().append(Message.body("Authentication failure").toString());
        };
    }

    private AuthenticationEntryPoint authEntryPoint() {
        return (request, response, e) -> {
            response.setStatus(401);
            response.getWriter().append(Message.body("Authentication required").toString());
        };
    }
}
