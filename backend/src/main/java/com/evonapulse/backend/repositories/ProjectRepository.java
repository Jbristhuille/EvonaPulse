package com.evonapulse.backend.repositories;

import com.evonapulse.backend.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<ProjectEntity, UUID> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, UUID id);
    Optional<ProjectEntity> getByApiKey(String apiKey);
}
