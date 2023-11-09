package dev.farneser.tasktracker.api.config.mapping;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AssemblyMappingProcessor implements CommandLineRunner {
    private final List<ITypeMapper> types;
    private final ModelMapper modelMapper;

    @Override
    public void run(String... args) {
        for (var type : types) {
            type.mapping(modelMapper);
        }
    }
}
