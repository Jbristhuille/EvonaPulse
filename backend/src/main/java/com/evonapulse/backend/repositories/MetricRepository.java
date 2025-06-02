package com.evonapulse.backend.repositories;

import com.evonapulse.backend.entities.MetricEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MetricRepository extends JpaRepository<MetricEntity, UUID> {
    boolean existsByNameAndProject_Id(String name, UUID projectId);
    List<MetricEntity> findByProject_Id(UUID projectId);
}