package com.core.monitoring.rest.impl;

import com.core.models.Process;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultProcessMonitoringServiceRestClientTest {

    @Mock
    private RestTemplate restTemplate;

    private DefaultProcessMonitoringServiceRestClient underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new DefaultProcessMonitoringServiceRestClient(restTemplate);
        ReflectionTestUtils.setField(underTest, "basePath", "");
    }

    @Test
    public void postProcesses_processesAreSent_success() {
        final List<Process> processList = List.of(new Process());
        final ResponseEntity<?> responseEntity = new ResponseEntity(HttpStatus.OK);

        when(restTemplate.postForObject(DefaultProcessMonitoringServiceRestClient.URL_PROCESS_MONITORING_SERVICE, processList, ResponseEntity.class)).thenReturn(responseEntity);

        underTest.postProcesses(processList);

        verify(restTemplate).postForObject(DefaultProcessMonitoringServiceRestClient.URL_PROCESS_MONITORING_SERVICE, processList, ResponseEntity.class);
        verifyNoMoreInteractions(restTemplate);
    }

    @Test(expected = IllegalStateException.class)
    public void postProcesses_processesAreNotSent_exception() {
        final List<Process> processList = List.of(new Process());
        final ResponseEntity<?> responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);

        when(restTemplate.postForObject(DefaultProcessMonitoringServiceRestClient.URL_PROCESS_MONITORING_SERVICE, processList, ResponseEntity.class)).thenReturn(responseEntity);

        underTest.postProcesses(processList);
    }
}