package com.evonapulse.backend.controllers;

import com.evonapulse.backend.dtos.ProjectCreateRequest;
import com.evonapulse.backend.dtos.ProjectPublicResponse;
import com.evonapulse.backend.dtos.ProjectUpdateRequest;
import com.evonapulse.backend.entities.ProjectEntity;
import com.evonapulse.backend.exceptions.ApiException;
import com.evonapulse.backend.mappers.ProjectMapper;
import com.evonapulse.backend.services.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectControllerTest {

    @Mock private ProjectService projectService;
    @Mock private ProjectMapper projectMapper;

    @InjectMocks private ProjectController projectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProjectsReturnsList() {
        when(projectService.getAll()).thenReturn(Collections.emptyList());
        List<ProjectPublicResponse> result = projectController.getAllProjects();
        assertNotNull(result);
    }

    @Test
    void testGetProjectByIdFound() {
        UUID id = UUID.randomUUID();

        ProjectEntity entity = new ProjectEntity();
        entity.setId(id);
        entity.setName("Test project");

        ProjectPublicResponse expectedResponse = new ProjectPublicResponse();
        expectedResponse.setId(String.valueOf(id));
        expectedResponse.setName("Test project");

        when(projectService.getById(id)).thenReturn(Optional.of(entity));
        when(projectMapper.toProjectPublicResponse(entity)).thenReturn(expectedResponse);

        ResponseEntity<ProjectPublicResponse> response = projectController.getProjectById(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testGetProjectByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(projectService.getById(id)).thenReturn(Optional.empty());

        ResponseEntity<ProjectPublicResponse> response = projectController.getProjectById(id);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testCreateProjectSuccess() {
        ProjectCreateRequest req = new ProjectCreateRequest();
        req.setName("Test Project");

        ProjectEntity entity = new ProjectEntity();
        ProjectPublicResponse response = new ProjectPublicResponse();

        when(projectService.nameExists(req.getName())).thenReturn(false);
        when(projectService.create(req)).thenReturn(entity);
        when(projectMapper.toProjectPublicResponse(entity)).thenReturn(response);

        ResponseEntity<ProjectPublicResponse> result = projectController.createProject(req);

        assertEquals(201, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testCreateProjectNameConflict() {
        ProjectCreateRequest req = new ProjectCreateRequest();
        req.setName("Conflict");

        when(projectService.nameExists(req.getName())).thenReturn(true);

        ApiException ex = assertThrows(ApiException.class, () -> projectController.createProject(req));
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("A project with this name already exists", ex.getMessage());

    }

    @Test
    void testUpdateProjectSuccess() {
        UUID id = UUID.randomUUID();
        ProjectUpdateRequest req = new ProjectUpdateRequest();
        req.setName("Updated");

        ProjectEntity updatedEntity = new ProjectEntity();
        updatedEntity.setId(id);
        updatedEntity.setName("Updated");

        ProjectPublicResponse expectedResponse = new ProjectPublicResponse();
        expectedResponse.setId(String.valueOf(id));
        expectedResponse.setName("Updated");

        when(projectService.existsById(id)).thenReturn(true);
        when(projectService.nameExistsExcludingId(req.getName(), id)).thenReturn(false);
        when(projectService.update(id, req)).thenReturn(updatedEntity);
        when(projectMapper.toProjectPublicResponse(updatedEntity)).thenReturn(expectedResponse);

        ResponseEntity<ProjectPublicResponse> response = projectController.updateProject(id, req);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testUpdateProjectNotFound() {
        UUID id = UUID.randomUUID();
        ProjectUpdateRequest req = new ProjectUpdateRequest();
        req.setName("Updated");

        when(projectService.existsById(id)).thenReturn(false);

        ResponseEntity<?> response = projectController.updateProject(id, req);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testUpdateProjectNameConflict() {
        UUID id = UUID.randomUUID();
        ProjectUpdateRequest req = new ProjectUpdateRequest();
        req.setName("Duplicate");

        when(projectService.existsById(id)).thenReturn(true);
        when(projectService.nameExistsExcludingId(req.getName(), id)).thenReturn(true);

        ApiException ex = assertThrows(ApiException.class, () -> projectController.updateProject(id, req));
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("A project with this name already exists", ex.getMessage());

    }

    @Test
    void testDeleteProjectSuccess() {
        UUID id = UUID.randomUUID();
        when(projectService.delete(id)).thenReturn(true);

        ResponseEntity<Map<String, String>> response = projectController.deleteProject(id);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeleteProjectNotFound() {
        UUID id = UUID.randomUUID();
        when(projectService.delete(id)).thenReturn(false);

        ResponseEntity<Map<String, String>> response = projectController.deleteProject(id);
        assertEquals(404, response.getStatusCodeValue());
    }
}
