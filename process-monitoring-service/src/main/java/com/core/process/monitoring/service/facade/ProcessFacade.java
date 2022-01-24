package com.core.process.monitoring.service.facade;


import java.util.List;

import com.core.models.Process;

public interface ProcessFacade {
    /**
     * Save processes information in the system
     *
     * @param processes - the list of processes
     */
    void saveAll(List<Process> processes);

    /**
     * Turn on/off the capacity for updating process information from external services
     *
     * @param state - true - enable refresh, false - disable refresh
     */
    void switchStateOfProcessesRefresh(boolean state);

    /**
     * Return all list of processes information
     *
     * @return the list of processes
     */
    List<Process> getAll();

}
