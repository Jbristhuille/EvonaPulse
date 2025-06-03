package com.evonapulse.backend.services;

import com.evonapulse.backend.dtos.LogIngestionRequest;
import com.evonapulse.backend.entities.LogEntity;
import com.evonapulse.backend.entities.MetricEntity;
import com.evonapulse.backend.repositories.LogRepository;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public LogEntity create(LogIngestionRequest req, MetricEntity metric) {
        LogEntity log = new LogEntity();
        log.setType(req.getType());
        log.setValue(req.getValue());
        log.setMetric(metric);
        return logRepository.save(log);
    }
}
