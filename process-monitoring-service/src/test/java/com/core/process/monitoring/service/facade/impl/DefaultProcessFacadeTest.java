package com.core.process.monitoring.service.facade.impl;

import com.core.models.Process;
import com.core.process.monitoring.service.converter.Converter;
import com.core.process.monitoring.service.facade.ProcessRefreshContext;
import com.core.process.monitoring.service.model.ProcessModel;
import com.core.process.monitoring.service.repository.ProcessRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultProcessFacadeTest {
    @Mock
    private ProcessRepository processRepository;

    @Mock
    private Converter<ProcessModel, Process> processConverter;

    @Mock
    private ProcessRefreshContext context;

    @InjectMocks
    private DefaultProcessFacade underTest;

    @Test
    public void saveAll_updatingEnable_saved() {

        final Process parentProcess = new Process();

        when(context.getRefreshEnable()).thenReturn(new AtomicBoolean(true));
        when(processConverter.reverseConvert(parentProcess)).thenReturn(new ProcessModel());

        underTest.saveAll(List.of(parentProcess));

        verify(context).getRefreshEnable();
        verify(processConverter).reverseConvert(parentProcess);
        verify(processRepository).saveAll(anyList());
        verify(processRepository).deleteAllByParentProcessIsNull();
        verifyNoMoreInteractions(context, processConverter, processRepository);
    }

    @Test
    public void saveAll_updatingDisable_notSaved() {

        final Process parentProcess = new Process();

        when(context.getRefreshEnable()).thenReturn(new AtomicBoolean(false));
        when(processConverter.reverseConvert(parentProcess)).thenReturn(new ProcessModel());

        underTest.saveAll(List.of(parentProcess));

        verify(context).getRefreshEnable();
        verifyNoMoreInteractions(context);
        verifyNoInteractions(processConverter, processRepository);
    }

    @Test
    public void switchStateOfProcessesRefresh_success() {
        underTest.switchStateOfProcessesRefresh(true);

        verify(context).setRefreshEnable(any(AtomicBoolean.class));
        verifyNoMoreInteractions(context);
        verifyNoInteractions(processConverter, processRepository);
    }

    @Test
    public void getAll_success() {
        final ProcessModel parentProcess = new ProcessModel();

        when(processRepository.findAll()).thenReturn(Collections.singletonList(parentProcess));
        when(processConverter.convert(parentProcess)).thenReturn(new Process());

        List<Process> all = underTest.getAll();

        Assert.assertTrue(all != null && !all.isEmpty());
        verify(processRepository).findAll();
        verify(processConverter).convert(parentProcess);
        verifyNoMoreInteractions(processConverter, processRepository);
        verifyNoInteractions(context);
    }

}