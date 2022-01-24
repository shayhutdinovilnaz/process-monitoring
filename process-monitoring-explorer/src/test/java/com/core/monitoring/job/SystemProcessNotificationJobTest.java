package com.core.monitoring.job;

import com.core.monitoring.converter.impl.ProcessConverter;
import com.core.monitoring.model.ProcessModel;
import com.core.monitoring.rest.ProcessMonitoringServiceRestClient;
import com.core.monitoring.service.OperationSystemInfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import com.core.models.Process;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SystemProcessNotificationJobTest {
    private static final String SYSTEM_OS = "UNIX";

    @Mock
    private ProcessMonitoringServiceRestClient processMonitoringRestService;

    @Mock
    private ProcessConverter processConverter;

    @Mock
    private OperationSystemInfoService operationSystemInfoService;

    private SystemProcessNotificationJob underTest;

    @Before
    public void setUp() {
        final Map<String, OperationSystemInfoService> operationSystemInfoServices = new HashMap<>();
        operationSystemInfoServices.put(SYSTEM_OS, operationSystemInfoService);

        underTest = new SystemProcessNotificationJob(processMonitoringRestService, operationSystemInfoServices, processConverter);
        ReflectionTestUtils.setField(underTest, "os", SYSTEM_OS);
    }

    @Test
    public void notifyAboutProcesses_sendInformation_success() {
        final ProcessModel processModel = new ProcessModel();
        final Process process = new Process();
        final List<ProcessModel> processModelList = List.of(processModel);

        when(operationSystemInfoService.getProcesses()).thenReturn(processModelList);
        when(processConverter.convert(processModel)).thenReturn(process);

        underTest.notifyAboutProcesses();

        verify(operationSystemInfoService).getProcesses();
        verify(processConverter).convert(processModel);
        verify(processMonitoringRestService).postProcesses(anyList());
        verifyNoMoreInteractions(operationSystemInfoService, processConverter, processMonitoringRestService);
    }
}