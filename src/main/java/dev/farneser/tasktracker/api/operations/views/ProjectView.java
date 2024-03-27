package dev.farneser.tasktracker.api.operations.views;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

@Data
@Slf4j
@Schema(name = "ProjectView", description = "Project view")
public class ProjectView implements ITypeMapper {
    @Schema(name = "id", description = "Project id", example = "1")
    private Long id;
    @Schema(name = "columnName", description = "Project name", example = "Best project name")
    private String projectName;

    public void mapping(ModelMapper modelMapper) {

    }
}
