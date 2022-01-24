package com.core.monitoring.service;

import com.core.monitoring.model.ProcessModel;

import java.util.List;

public interface OperationSystemInfoService {
    /**
     * Provide information about all processes in the Operating System.
     *
     * @return the list of process
     */
    List<ProcessModel> getProcesses();
}
