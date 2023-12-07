package dev.farneser.tasktracker.api.config.mapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AssemblyMappingProcessor implements CommandLineRunner {
    private final List<ITypeMapper> types;
    private final ModelMapper modelMapper;

    @Override
    public void run(String... args) {
        log.debug("Mapping types");

        for (var type : types) {
            log.debug("Mapping type {}", type.getClass().getSimpleName());

            type.mapping(modelMapper);
        }

        log.debug("Types mapped");
    }
}
