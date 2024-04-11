package dev.farneser.tasktracker.api.web.dto.project;

import dev.farneser.tasktracker.api.config.mapping.ITypeMapper;
import dev.farneser.tasktracker.api.operations.commands.project.patchmember.PatchProjectMemberCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

@Data
@Slf4j
@Schema(name = "PatchProjectMemberDto", description = "Patch project member DTO")
public class PatchProjectMemberDto implements ITypeMapper {
    private Long memberId;
    private String role;

    @Override
    public void mapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(PatchProjectMemberDto.class, PatchProjectMemberCommand.class)
                .addMapping(PatchProjectMemberDto::getRole, PatchProjectMemberCommand::setRole)
                .addMapping(PatchProjectMemberDto::getMemberId, PatchProjectMemberCommand::setMemberId);
    }
}
