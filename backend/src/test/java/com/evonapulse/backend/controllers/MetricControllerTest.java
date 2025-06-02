package com.evonapulse.backend.controllers;

import com.evonapulse.backend.dtos.MetricCreateRequest;
import com.evonapulse.backend.dtos.MetricPublicResponse;
import com.evonapulse.backend.entities.MetricEntity;
import com.evonapulse.backend.exceptions.ApiException;
import com.evonapulse.backend.mappers.MetricMapper;
import com.evonapulse.backend.services.MetricService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MetricControllerTest {

    @Mock private MetricService metricService;
    @Mock private MetricMapper metricMapper;

    @InjectMocks private MetricController metricController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMetricsReturnsList() {
        UUID projectId = UUID.randomUUID();
        List<MetricEntity> entities = List.of(new MetricEntity(), new MetricEntity());
        List<MetricPublicResponse> expected = List.of(new MetricPublicResponse(), new MetricPublicResponse());

        when(metricService.getAllByProjectId(projectId)).thenReturn(entities);
        when(metricMapper.toPublicResponse(any())).thenReturn(expected.get(0), expected.get(1));

        List<MetricPublicResponse> result = metricController.getAllMetrics(projectId);

        assertEquals(2, result.size());
    }

    @Test
    void testGetMetricByIdFound() {
        UUID projectId = UUID.randomUUID();
        UUID metricId = UUID.randomUUID();

        MetricEntity entity = new MetricEntity();
        MetricPublicResponse expected = new MetricPublicResponse();

        when(metricService.getByIdAndProjectId(metricId, projectId)).thenReturn(Optional.of(entity));
        when(metricMapper.toPublicResponse(entity)).thenReturn(expected);

        ResponseEntity<MetricPublicResponse> response = metricController.getMetricById(projectId, metricId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expected, response.getBody());
    }

    @Test
    void testGetMetricByIdNotFound() {
        UUID projectId = UUID.randomUUID();
        UUID metricId = UUID.randomUUID();

        when(metricService.getByIdAndProjectId(metricId, projectId)).thenReturn(Optional.empty());

        ApiException ex = assertThrows(ApiException.class, () -> metricController.getMetricById(projectId, metricId));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Metric not found", ex.getMessage());
    }

    @Test
    void testCreateMetricSuccess() {
        UUID projectId = UUID.randomUUID();
        MetricCreateRequest request = new MetricCreateRequest();

        MetricEntity created = new MetricEntity();
        MetricPublicResponse response = new MetricPublicResponse();

        when(metricService.create(projectId, request)).thenReturn(created);
        when(metricMapper.toPublicResponse(created)).thenReturn(response);

        ResponseEntity<MetricPublicResponse> result = metricController.createMetric(projectId, request);

        assertEquals(201, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testDeleteMetricSuccess() {
        UUID projectId = UUID.randomUUID();
        UUID metricId = UUID.randomUUID();

        when(metricService.deleteByIdAndProjectId(metricId, projectId)).thenReturn(true);

        ResponseEntity<Map<String, String>> response = metricController.deleteMetric(projectId, metricId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Metric deleted !", response.getBody().get("message"));
    }

    @Test
    void testDeleteMetricNotFound() {
        UUID projectId = UUID.randomUUID();
        UUID metricId = UUID.randomUUID();

        when(metricService.deleteByIdAndProjectId(metricId, projectId)).thenReturn(false);

        ApiException ex = assertThrows(ApiException.class, () -> metricController.deleteMetric(projectId, metricId));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Metric not found", ex.getMessage());
    }
}
