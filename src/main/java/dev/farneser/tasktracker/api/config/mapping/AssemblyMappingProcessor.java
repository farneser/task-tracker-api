package dev.farneser.tasktracker.api.config.mapping;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssemblyMappingProcessor implements CommandLineRunner {
    private final List<ITypeMapper> types;
    private final ModelMapper modelMapper;

    @Autowired
    public AssemblyMappingProcessor(List<ITypeMapper> types, ModelMapper modelMapper) {
        this.types = types;
        this.modelMapper = modelMapper;
    }

    @Override
    public void run(String... args) {
        for (var type : types) {
            type.mapping(modelMapper);
        }
    }
}
