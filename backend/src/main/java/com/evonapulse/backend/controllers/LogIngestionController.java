package com.evonapulse.backend.controllers;

import com.evonapulse.backend.dtos.LogIngestionRequest;
import com.evonapulse.backend.dtos.LogPublicResponse;
import com.evonapulse.backend.entities.MetricEntity;
import com.evonapulse.backend.entities.ProjectEntity;
import com.evonapulse.backend.exceptions.ApiException;
import com.evonapulse.backend.mappers.LogMapper;
import com.evonapulse.backend.services.LogService;
import com.evonapulse.backend.services.MetricService;
import com.evonapulse.backend.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ingest")
public class LogIngestionController {
    private final ProjectService projectService;
    private final MetricService metricService;
    private final LogService logService;
    private final LogMapper logMapper;

    public LogIngestionController(ProjectService projectService,
                                  MetricService metricService,
                                  LogService logService,
                                  LogMapper logMapper) {
        this.projectService = projectService;
        this.metricService = metricService;
        this.logService = logService;
        this.logMapper = logMapper;
    }

    @PostMapping("/{projectApikey}")
    public ResponseEntity<LogPublicResponse> ingestLog(
            @PathVariable("projectApikey") String apikey,
            @Valid @RequestBody LogIngestionRequest request) {
        ProjectEntity project = projectService.getByApikey(apikey)
                .orElseThrow(() -> new ApiException("Project not found with this apikey", HttpStatus.NOT_FOUND));

        MetricEntity metric = metricService.getByNameAndProjectId(request.getMetricName(), project.getId())
                .orElseThrow(() -> new ApiException("Metric not found", HttpStatus.NOT_FOUND));

        if(metric.getType() != request.getType()) {
            throw new ApiException("Invalid metric type", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(logMapper.toPublicResponse(logService.create(request, metric)));
    }
}
