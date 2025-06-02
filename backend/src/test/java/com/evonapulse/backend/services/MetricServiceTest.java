package com.evonapulse.backend.services;

import com.evonapulse.backend.dtos.MetricCreateRequest;
import com.evonapulse.backend.entities.MetricEntity;
import com.evonapulse.backend.entities.ProjectEntity;
import com.evonapulse.backend.exceptions.ApiException;
import com.evonapulse.backend.mappers.MetricMapper;
import com.evonapulse.backend.repositories.MetricRepository;
import com.evonapulse.backend.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MetricServiceTest {

    private MetricService metricService;
    private MetricRepository metricRepository;
    private ProjectRepository projectRepository;
    private MetricMapper metricMapper;

    @BeforeEach
    void setUp() {
        metricRepository = mock(MetricRepository.class);
        projectRepository = mock(ProjectRepository.class);
        metricMapper = mock(MetricMapper.class);
        metricService = new MetricService(metricRepository, projectRepository, metricMapper);
    }

    @Test
    void testGetAllByProjectId() {
        UUID projectId = UUID.randomUUID();
        ProjectEntity project = new ProjectEntity();
        project.setId(projectId);

        MetricEntity m1 = new MetricEntity();
        m1.setProject(project);
        MetricEntity m2 = new MetricEntity();
        m2.setProject(project);

        when(metricRepository.findByProject_Id(projectId)).thenReturn(List.of(m1, m2));

        List<MetricEntity> result = metricService.getAllByProjectId(projectId);
        assertEquals(2, result.size());
    }

    @Test
    void testGetByIdAndProjectIdFound() {
        UUID metricId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();

        ProjectEntity project = new ProjectEntity();
        project.setId(projectId);

        MetricEntity metric = new MetricEntity();
        metric.setId(metricId);
        metric.setProject(project);

        when(metricRepository.findById(metricId)).thenReturn(Optional.of(metric));

        Optional<MetricEntity> result = metricService.getByIdAndProjectId(metricId, projectId);
        assertTrue(result.isPresent());
        assertEquals(metric, result.get());
    }

    @Test
    void testCreateSuccess() {
        UUID projectId = UUID.randomUUID();
        MetricCreateRequest req = new MetricCreateRequest();
        ProjectEntity project = new ProjectEntity();
        project.setId(projectId);
        MetricEntity metric = new MetricEntity();

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(metricMapper.fromCreateDto(req, project)).thenReturn(metric);
        when(metricRepository.save(metric)).thenReturn(metric);

        MetricEntity result = metricService.create(projectId, req);
        assertEquals(metric, result);
    }

    @Test
    void testCreateFailsWhenProjectNotFound() {
        UUID projectId = UUID.randomUUID();
        MetricCreateRequest req = new MetricCreateRequest();

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> metricService.create(projectId, req));
    }

    @Test
    void testDeleteByIdAndProjectIdSuccess() {
        UUID metricId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();

        ProjectEntity project = new ProjectEntity();
        project.setId(projectId);

        MetricEntity metric = new MetricEntity();
        metric.setId(metricId);
        metric.setProject(project);

        when(metricRepository.findById(metricId)).thenReturn(Optional.of(metric));

        boolean result = metricService.deleteByIdAndProjectId(metricId, projectId);
        assertTrue(result);
        verify(metricRepository).deleteById(metricId);
    }

    @Test
    void testDeleteByIdAndProjectIdNotFound() {
        UUID metricId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();

        when(metricRepository.findById(metricId)).thenReturn(Optional.empty());

        boolean result = metricService.deleteByIdAndProjectId(metricId, projectId);
        assertFalse(result);
    }
}
