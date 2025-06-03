package com.evonapulse.backend.repositories;

import com.evonapulse.backend.entities.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LogRepository extends JpaRepository<LogEntity, UUID> {
}
