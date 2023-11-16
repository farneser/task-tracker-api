package dev.farneser.tasktracker.api.config;

import dev.farneser.tasktracker.api.mediator.Mediator;
import dev.farneser.tasktracker.api.mediator.DefaultMediator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class CommonsConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    @Autowired
    public Mediator mediator(ApplicationContext applicationContext) {
        return new DefaultMediator(applicationContext);
    }
}