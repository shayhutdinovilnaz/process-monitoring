package com.core.monitoring.rest.impl;

import com.core.models.Process;
import com.core.monitoring.rest.ProcessMonitoringServiceRestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class DefaultProcessMonitoringServiceRestClient implements ProcessMonitoringServiceRestClient {
    public static final String URL_PROCESS_MONITORING_SERVICE = "/api/v1/process/saveAll";

    private final RestTemplate restTemplate;

    @Value("${process.monitoring.service.host}")
    private String basePath;

    @Override
    @Retryable(value = RuntimeException.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    public void postProcesses(List<Process> process) {
        final ResponseEntity<?> responseEntity = restTemplate.postForObject(basePath + URL_PROCESS_MONITORING_SERVICE, process, ResponseEntity.class);
        if (responseEntity != null && responseEntity.getStatusCode() != HttpStatus.OK) {
            log.error("The service could not send information about updated processes. Status code: {}", responseEntity.getStatusCode());
            throw new IllegalStateException("The service could not send information about updated processes.");
        }

        log.info("Updated processes are sent.");
    }
}
