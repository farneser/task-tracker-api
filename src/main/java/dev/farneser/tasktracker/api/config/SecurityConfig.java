package dev.farneser.tasktracker.api.config;

import dev.farneser.tasktracker.api.filters.JwtAuthenticationFilter;
import dev.farneser.tasktracker.api.service.UserService;
import dev.farneser.tasktracker.api.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Autowired
    public SecurityConfig(UserService userService) {
        this.userService = userService;
        this.jwtUtils = new JwtUtils();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/auth/**").permitAll();
                    req.requestMatchers(HttpMethod.GET, "/styles/**", "/scripts/**").permitAll();
                    req.anyRequest().authenticated();
                })
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtils, userService), UsernamePasswordAuthenticationFilter.class)
                .formLogin(login -> {
                    login.usernameParameter("username");
                    login.passwordParameter("password");
                    login.loginPage("/auth/login");
                    login.permitAll();
                })
                .logout(LogoutConfigurer::permitAll)
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
