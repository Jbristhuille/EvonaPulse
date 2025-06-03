package com.evonapulse.backend.repositories;

import com.evonapulse.backend.entities.MetricEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MetricRepository extends JpaRepository<MetricEntity, UUID> {
    boolean existsByName(String name);
    boolean existsByNameAndProjectId(String name, UUID project);
    List<MetricEntity> findByProject_Id(UUID projectId);
    Optional<MetricEntity> getByNameAndProjectId(String name, UUID projectId);
}