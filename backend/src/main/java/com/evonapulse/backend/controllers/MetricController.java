package com.evonapulse.backend.controllers;

import com.evonapulse.backend.dtos.MetricCreateRequest;
import com.evonapulse.backend.dtos.MetricPublicResponse;
import com.evonapulse.backend.entities.MetricEntity;
import com.evonapulse.backend.exceptions.ApiException;
import com.evonapulse.backend.mappers.MetricMapper;
import com.evonapulse.backend.services.MetricService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects/{projectId}/metrics")
public class MetricController {

    private final MetricService metricService;
    private final MetricMapper metricMapper;

    public MetricController(MetricService metricService,
                            MetricMapper metricMapper) {
        this.metricService = metricService;
        this.metricMapper = metricMapper;
    }

    @GetMapping
    public List<MetricPublicResponse> getAllMetrics(@PathVariable UUID projectId) {
        return metricService.getAllByProjectId(projectId).stream()
                .map(metricMapper::toPublicResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetricPublicResponse> getMetricById(@PathVariable UUID projectId,
                                                              @PathVariable UUID id) {
        return metricService.getByIdAndProjectId(id, projectId)
                .map(metricMapper::toPublicResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ApiException("Metric not found", HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<MetricPublicResponse> createMetric(@PathVariable UUID projectId,
                                                             @Valid @RequestBody MetricCreateRequest request) {
        if (metricService.nameExistsInProject(request.getName(), projectId)) {
            throw new ApiException("A metric with this name already exists", HttpStatus.CONFLICT);
        }

        MetricEntity created = metricService.create(projectId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(metricMapper.toPublicResponse(created));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteMetric(@PathVariable UUID projectId,
                                             @PathVariable UUID id) {
        boolean deleted = metricService.deleteByIdAndProjectId(id, projectId);
        if (!deleted) {
            throw new ApiException("Metric not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(Map.of("message", "Metric deleted!"));
    }
}
