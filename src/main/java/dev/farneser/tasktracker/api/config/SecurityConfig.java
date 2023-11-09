package dev.farneser.tasktracker.api.config;

import dev.farneser.tasktracker.api.filters.JwtAuthenticationFilter;
import dev.farneser.tasktracker.api.models.Message;
import dev.farneser.tasktracker.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationFilter jwtAuthFilter;

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/register", "/api/v1/auth/authenticate",};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> {
                            req.requestMatchers(WHITE_LIST_URL)
                                    .permitAll();
                            req.requestMatchers("/error").permitAll();
                            req.anyRequest().authenticated();
                        }
                )
                .formLogin(login -> {
                    login.successHandler(this.successAuth());
                    login.failureHandler(this.failureAuth());
                    login.loginProcessingUrl("/api/v1/auth/authenticate");
                    login.usernameParameter("username");
                    login.passwordParameter("password");
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                                logout.logoutUrl("/api/v1/auth/logout")
//                                .addLogoutHandler(logoutHandler)
                                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )
                .exceptionHandling(exceptions -> {
                    exceptions.authenticationEntryPoint(authEntryPoint());
                });


        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);

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
