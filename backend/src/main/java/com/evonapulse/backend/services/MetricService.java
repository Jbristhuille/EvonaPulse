package com.evonapulse.backend.services;

import com.evonapulse.backend.dtos.MetricCreateRequest;
import com.evonapulse.backend.entities.MetricEntity;
import com.evonapulse.backend.entities.ProjectEntity;
import com.evonapulse.backend.exceptions.ApiException;
import com.evonapulse.backend.mappers.MetricMapper;
import com.evonapulse.backend.repositories.MetricRepository;
import com.evonapulse.backend.repositories.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MetricService {

    private final MetricRepository metricRepository;
    private final ProjectRepository projectRepository;
    private final MetricMapper metricMapper;

    public MetricService(MetricRepository metricRepository,
                         ProjectRepository projectRepository,
                         MetricMapper metricMapper) {
        this.metricRepository = metricRepository;
        this.projectRepository = projectRepository;
        this.metricMapper = metricMapper;
    }

    public boolean nameExists(String name) {
        return metricRepository.existsByName(name);
    }

    public boolean existByNameAndProjectId(String name, UUID projectId) {
        return metricRepository.existsByNameAndProjectId(name, projectId);
    }

    public List<MetricEntity> getAllByProjectId(UUID projectId) {
        return metricRepository.findByProject_Id(projectId);
    }

    public Optional<MetricEntity> getByIdAndProjectId(UUID metricId, UUID projectId) {
        return metricRepository.findById(metricId)
                .filter(metric -> metric.getProject().getId().equals(projectId));
    }

    public Optional<MetricEntity> getByNameAndProjectId(String name, UUID projectId) {
        return metricRepository.getByNameAndProjectId(name, projectId)
                .filter(metric -> metric.getProject().getId().equals(projectId));
    }

    public MetricEntity create(UUID projectId, MetricCreateRequest req) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException("Project not found", HttpStatus.NOT_FOUND));

        MetricEntity metric = metricMapper.fromCreateDto(req, project);
        return metricRepository.save(metric);
    }


    public boolean deleteByIdAndProjectId(UUID metricId, UUID projectId) {
        Optional<MetricEntity> metric = metricRepository.findById(metricId)
                .filter(m -> m.getProject().getId().equals(projectId));

        if (metric.isPresent()) {
            metricRepository.deleteById(metricId);
            return true;
        }
        return false;
    }
}
