package com.evonapulse.backend.services;

import com.evonapulse.backend.dtos.MetricCreateRequest;
import com.evonapulse.backend.entities.MetricEntity;
import com.evonapulse.backend.entities.ProjectEntity;
import com.evonapulse.backend.mappers.MetricMapper;
import com.evonapulse.backend.repositories.MetricRepository;
import com.evonapulse.backend.repositories.ProjectRepository;
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

    public List<MetricEntity> getAll() {
        return metricRepository.findAll();
    }

    public Optional<MetricEntity> getById(UUID id) {
        return metricRepository.findById(id);
    }

    public MetricEntity create(MetricCreateRequest dto) {
        ProjectEntity project = projectRepository.findById(dto.getProjectId())
                .orElseThrow();

        MetricEntity metric = metricMapper.fromCreateDto(dto, project);
        return metricRepository.save(metric);
    }

    public boolean delete(UUID id) {
        if (metricRepository.existsById(id)) {
            metricRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
