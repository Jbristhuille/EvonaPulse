package com.evonapulse.backend.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class LogEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MetricType type;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String value;

    @ManyToOne(optional = false)
    @JoinColumn(name = "metric_id", nullable = false)
    private MetricEntity metric;
}
