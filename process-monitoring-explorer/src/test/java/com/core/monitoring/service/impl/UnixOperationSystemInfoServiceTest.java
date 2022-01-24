package com.core.monitoring.service.impl;

import com.core.monitoring.model.ProcessModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class UnixOperationSystemInfoServiceTest {

    private final UnixOperationSystemInfoService underTest = new UnixOperationSystemInfoService();

    @Test
    public void getProcesses_success() {
        List<ProcessModel> processes = underTest.getProcesses();
        Assert.assertTrue(processes.size() > 0);
    }
}