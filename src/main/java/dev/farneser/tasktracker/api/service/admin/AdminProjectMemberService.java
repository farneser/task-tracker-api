package dev.farneser.tasktracker.api.service.admin;

import dev.farneser.tasktracker.api.models.project.ProjectMember;
import dev.farneser.tasktracker.api.operations.views.pageable.Pageable;
import dev.farneser.tasktracker.api.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;

    public Pageable<ProjectMember> get(int page, int size) {

        Page<ProjectMember> projectSlice = projectMemberRepository.findAll(PageRequest.of(page, size));

        List<ProjectMember> projects = projectSlice.stream().peek(p -> {
            p.getMember().setProjects(null);
            p.getProject().setMembers(null);
            p.getProject().setStatuses(null);
        }).toList();

        return new Pageable<>(projects, projectSlice.getTotalPages(), projectSlice.getTotalElements());
    }
}
