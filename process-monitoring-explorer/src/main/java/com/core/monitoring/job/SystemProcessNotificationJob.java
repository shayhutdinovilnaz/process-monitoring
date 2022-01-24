package com.core.monitoring.job;

import com.core.models.Process;
import com.core.monitoring.converter.impl.ProcessConverter;
import com.core.monitoring.rest.ProcessMonitoringServiceRestClient;
import com.core.monitoring.service.OperationSystemInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SystemProcessNotificationJob {
    private final ProcessMonitoringServiceRestClient processMonitoringService;
    private final Map<String, OperationSystemInfoService> operationSystemInfoServices;
    private final ProcessConverter processConverter;

    @Value("${operation.system.name}")
    private String os;

    @Scheduled(fixedDelay = 10000)
    public void notifyAboutProcesses() {
        log.info("Job about process notifications is started.");
        List<Process> processes = operationSystemInfoServices.get(os)
                        .getProcesses()
                        .stream()
                        .map(processConverter::convert)
                        .collect(Collectors.toList());
        log.info("The count of active process in system: {}.", processes.size());

        processMonitoringService.postProcesses(processes);
        log.info("Job about process notifications is completed.");
    }
}
