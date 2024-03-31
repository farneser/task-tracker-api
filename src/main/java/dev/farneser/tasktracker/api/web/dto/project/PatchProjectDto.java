package dev.farneser.tasktracker.api.web.dto.project;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.project.patch.PatchProjectCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

@Data
@Slf4j
@Schema(name = "PatchProjectDto", description = "Patch project DTO")
public class PatchProjectDto implements ITypeMapper {
    @Length(min = 1, max = 255)
    @Schema(name = "projectName", description = "Project name", example = "New best project")
    private String projectName;

    @Override
    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(PatchProjectDto.class, PatchProjectCommand.class)
                .addMapping(PatchProjectDto::getProjectName, PatchProjectCommand::setProjectName);
    }
}
