package com.evonapulse.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class LogEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MetricType type;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String value;

    @ManyToOne(optional = false)
    @JoinColumn(name = "metric_id", nullable = false)
    private MetricEntity metric;
}
