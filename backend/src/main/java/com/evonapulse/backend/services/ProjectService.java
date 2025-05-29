package com.evonapulse.backend.services;

import com.evonapulse.backend.dtos.ProjectCreateRequest;
import com.evonapulse.backend.dtos.ProjectUpdateRequest;
import com.evonapulse.backend.entities.ProjectEntity;
import com.evonapulse.backend.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<ProjectEntity> getAll() {
        return projectRepository.findAll();
    }

    public Optional<ProjectEntity> getById(UUID id) {
        return projectRepository.findById(id);
    }

    public boolean existsById(UUID id) {
        return projectRepository.existsById(id);
    }

    public boolean nameExists(String name) {
        return projectRepository.existsByName(name);
    }

    public boolean nameExistsExcludingId(String name, UUID id) {
        return projectRepository.existsByNameAndIdNot(name, id);
    }

    public ProjectEntity create(ProjectCreateRequest request) {
        ProjectEntity project = new ProjectEntity();
        project.setName(request.getName());
        project.setApiKey(UUID.randomUUID().toString());
        return projectRepository.save(project);
    }

    public ProjectEntity update(UUID id, ProjectUpdateRequest request) {
        ProjectEntity project = projectRepository.findById(id).orElseThrow();
        project.setName(request.getName());
        project.setUpdatedAt(LocalDateTime.now());
        return projectRepository.save(project);
    }

    public boolean delete(UUID id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
