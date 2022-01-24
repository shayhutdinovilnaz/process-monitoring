package com.core.process.monitoring.service.facade.impl;

import com.core.models.Process;
import com.core.process.monitoring.service.converter.Converter;
import com.core.process.monitoring.service.facade.ProcessFacade;
import com.core.process.monitoring.service.facade.ProcessRefreshContext;
import com.core.process.monitoring.service.model.ProcessModel;
import com.core.process.monitoring.service.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class DefaultProcessFacade implements ProcessFacade {
    private final ProcessRepository processRepository;
    private final Converter<ProcessModel, Process> processConverter;
    private final ProcessRefreshContext context;

    @Override
    @Transactional
    public void saveAll(List<Process> processes) {
        if (context.getRefreshEnable().get()) {
            processRepository.deleteAllByParentProcessIsNull();
            log.info("Previous processes are removed.");
            List<ProcessModel> processModels = processes.stream().map(processConverter::reverseConvert).collect(Collectors.toList());
            processRepository.saveAll(processModels);
            log.info("Updated list of process is saved. Count: {}. ", processModels.size());
        } else {
            log.info("Refresh of processes are not available");
        }
    }

    @Override
    public void switchStateOfProcessesRefresh(boolean state) {
        context.setRefreshEnable(new AtomicBoolean(state));
        log.info("State of processes refresh is updated. State: {}", state);
    }

    @Override
    public List<Process> getAll() {
        return processRepository.findAll().stream().map(processConverter::convert).collect(Collectors.toList());
    }

}
