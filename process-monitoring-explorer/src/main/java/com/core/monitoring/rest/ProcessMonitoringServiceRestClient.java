package com.core.monitoring.rest;

import com.core.models.Process;
import java.util.List;

public interface ProcessMonitoringServiceRestClient {

    /**
     * Send list of process to Process Monitoring service.
     *
     * @param processes - the list of of processes for sending
     */
    void postProcesses(List<Process> processes);
}
