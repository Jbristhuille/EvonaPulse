package com.evonapulse.backend.services;

import com.evonapulse.backend.dtos.ProjectCreateRequest;
import com.evonapulse.backend.dtos.ProjectUpdateRequest;
import com.evonapulse.backend.entities.ProjectEntity;
import com.evonapulse.backend.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllReturnsList() {
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());
        List<ProjectEntity> result = projectService.getAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetByIdReturnsProject() {
        UUID id = UUID.randomUUID();
        ProjectEntity entity = new ProjectEntity();
        when(projectRepository.findById(id)).thenReturn(Optional.of(entity));
        Optional<ProjectEntity> result = projectService.getById(id);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void testExistsById() {
        UUID id = UUID.randomUUID();
        when(projectRepository.existsById(id)).thenReturn(true);
        assertTrue(projectService.existsById(id));
    }

    @Test
    void testNameExists() {
        when(projectRepository.existsByName("MyProject")).thenReturn(true);
        assertTrue(projectService.nameExists("MyProject"));
    }

    @Test
    void testNameExistsExcludingId() {
        UUID id = UUID.randomUUID();
        when(projectRepository.existsByNameAndIdNot("MyProject", id)).thenReturn(true);
        assertTrue(projectService.nameExistsExcludingId("MyProject", id));
    }

    @Test
    void testCreateProject() {
        ProjectCreateRequest request = new ProjectCreateRequest();
        request.setName("New Project");

        when(projectRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ProjectEntity result = projectService.create(request);

        assertEquals("New Project", result.getName());
        assertNotNull(result.getApiKey());
    }

    @Test
    void testUpdateProject() {
        UUID id = UUID.randomUUID();
        ProjectUpdateRequest request = new ProjectUpdateRequest();
        request.setName("Updated Name");

        ProjectEntity existing = new ProjectEntity();
        existing.setId(id);
        when(projectRepository.findById(id)).thenReturn(Optional.of(existing));
        when(projectRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ProjectEntity updated = projectService.update(id, request);

        assertEquals("Updated Name", updated.getName());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void testDeleteSuccess() {
        UUID id = UUID.randomUUID();
        when(projectRepository.existsById(id)).thenReturn(true);
        boolean result = projectService.delete(id);
        assertTrue(result);
        verify(projectRepository).deleteById(id);
    }

    @Test
    void testDeleteNotFound() {
        UUID id = UUID.randomUUID();
        when(projectRepository.existsById(id)).thenReturn(false);
        boolean result = projectService.delete(id);
        assertFalse(result);
        verify(projectRepository, never()).deleteById(any());
    }
}
