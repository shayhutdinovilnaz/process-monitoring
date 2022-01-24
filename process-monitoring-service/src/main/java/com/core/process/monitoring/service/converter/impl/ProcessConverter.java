package com.core.process.monitoring.service.converter.impl;

import com.core.models.Process;
import com.core.process.monitoring.service.converter.Converter;
import com.core.process.monitoring.service.model.ProcessModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProcessConverter implements Converter<ProcessModel, Process> {

    @Override
    public Process convert(ProcessModel source) {
        Objects.requireNonNull(source, "Source model for converting could not be nullable.");
        final Process target = new Process();
        target.pid(source.getPid());
        target.setUser(source.getUser());
        target.setUsedRAM(source.getUsedRAM());
        target.setUsedCPU(source.getUsedCPU());

        final List<Process> children = Optional.ofNullable(source.getChildren())
                .orElseGet(ArrayList::new)
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
        target.setChildren(children);

        return target;
    }

    @Override
    public ProcessModel reverseConvert(Process source) {
        Objects.requireNonNull(source, "Source model for converting could not be nullable.");
        final ProcessModel target = new ProcessModel();
        target.setPid(source.getPid());
        target.setUser(source.getUser());
        target.setUsedRAM(source.getUsedRAM());
        target.setUsedCPU(source.getUsedCPU());

        final List<ProcessModel> children = Optional.ofNullable(source.getChildren())
                .orElseGet(ArrayList::new)
                .stream()
                .map(this::reverseConvert)
                .peek(processModel -> {
                    processModel.setParentProcess(target);
                })
                .collect(Collectors.toList());

        target.setChildren(children);

        return target;
    }
}
