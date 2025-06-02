package com.evonapulse.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
public class MetricEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String label;

    @NotBlank
    @Pattern(regexp = "^[a-z0-9_-]+$", message = "The name must be lowercase, without accents, and may only contain hyphens or underscores.")
    private String name;

    @Enumerated(EnumType.STRING)
    private MetricType type;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public MetricEntity() {
    }
}
