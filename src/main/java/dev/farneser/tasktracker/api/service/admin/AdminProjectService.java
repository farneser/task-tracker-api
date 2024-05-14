package dev.farneser.tasktracker.api.service.admin;

import dev.farneser.tasktracker.api.models.project.Project;
import dev.farneser.tasktracker.api.operations.views.pageable.Pageable;
import dev.farneser.tasktracker.api.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminProjectService {
    private final ProjectRepository projectRepository;

    public Pageable<Project> get(int page, int size) {

        Page<Project> projectSlice = projectRepository.findAll(PageRequest.of(page, size));

        List<Project> projects = projectSlice.stream().peek(p -> {
            p.setMembers(null);
            p.setStatuses(null);
        }).toList();

        return new Pageable<>(projects, projectSlice.getTotalPages(), projectSlice.getTotalElements());
    }
}
