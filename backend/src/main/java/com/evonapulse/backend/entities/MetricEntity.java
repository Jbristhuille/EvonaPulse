package com.evonapulse.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
public class MetricEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String label;

    @NotBlank
    @Pattern(regexp = "^[a-z0-9\\-_]+$", message = "The name must be lowercase, without accents, and may only contain hyphens or underscores.")
    private String name; // Nom technique (ex: "avg_response_time")

    @Enumerated(EnumType.STRING)
    private MetricType type;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
