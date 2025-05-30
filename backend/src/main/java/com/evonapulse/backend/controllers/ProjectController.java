package com.evonapulse.backend.controllers;

import com.evonapulse.backend.dtos.ProjectCreateRequest;
import com.evonapulse.backend.dtos.ProjectPublicResponse;
import com.evonapulse.backend.dtos.ProjectUpdateRequest;
import com.evonapulse.backend.entities.ProjectEntity;
import com.evonapulse.backend.exceptions.ApiException;
import com.evonapulse.backend.mappers.ProjectMapper;
import com.evonapulse.backend.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    public ProjectController(ProjectService projectService,
                             ProjectMapper projectMapper) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
    }

    @GetMapping
    public List<ProjectEntity> getAllProjects() {
        return projectService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectEntity> getProjectById(@PathVariable UUID id) {
        return projectService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProjectPublicResponse> createProject(@Valid @RequestBody ProjectCreateRequest request) {
        if (projectService.nameExists(request.getName())) {
            throw new ApiException("A project with this name already exists", HttpStatus.CONFLICT);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(projectMapper.toProjectPublicResponse(projectService.create(request)));

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable UUID id,
                                           @Valid @RequestBody ProjectUpdateRequest request) {
        if (!projectService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        if (projectService.nameExistsExcludingId(request.getName(), id)) {
            throw new ApiException("A project with this name already exists", HttpStatus.CONFLICT);
        }

        ProjectEntity updated = projectService.update(id, request);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        boolean deleted = projectService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
