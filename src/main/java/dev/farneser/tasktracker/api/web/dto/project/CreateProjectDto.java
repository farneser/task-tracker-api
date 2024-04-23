package dev.farneser.tasktracker.api.web.dto.project;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.project.create.CreateProjectCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "CreateProjectDto", description = "Create project DTO")
public class CreateProjectDto implements ITypeMapper {
    @Length(min = 1, max = 255)
    @Schema(name = "projectName", description = "Project name", example = "Best project")
    private String projectName;

    @Override
    public void mapping(ModelMapper modelMapper) {
        log.debug("Mapping CreateStatusDto");

        modelMapper.createTypeMap(CreateProjectDto.class, CreateProjectCommand.class)
                .addMapping(CreateProjectDto::getProjectName, CreateProjectCommand::setProjectName);
    }
}
