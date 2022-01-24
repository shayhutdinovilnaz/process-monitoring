package com.core.monitoring.service.impl;

import com.core.monitoring.model.ProcessModel;
import com.core.monitoring.service.OperationSystemInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class UnixOperationSystemInfoService implements OperationSystemInfoService {
    private static final String CMD_USAGE = "ps -p %s -o";

    @Override
    public List<ProcessModel> getProcesses() {
        return ProcessHandle.allProcesses()
                .filter(p -> p.parent().isEmpty() && p.pid() != 0)
                .map(this::mapping)
                .collect(Collectors.toList());
    }

    private ProcessModel mapping(ProcessHandle sx) {
        final ProcessModel processModel = new ProcessModel();
        processModel.setPid(sx.pid());
        processModel.setUser(sx.info().user().orElse(null));

        final List<ProcessModel> children = Optional.ofNullable(sx.children())
                .orElseGet(Stream::empty)
                .filter(ProcessHandle::isAlive)
                .map(this::mapping)
                .collect(Collectors.toList());
        processModel.setChildren(children);

        processModel.setUsedCPU(execCmd(String.format(CMD_USAGE, processModel.getPid()) + " %cpu=") + "%");
        processModel.setUsedRAM(execCmd(String.format(CMD_USAGE, processModel.getPid()) + " %mem=") + "%");

        return processModel;
    }

    private String execCmd(String cmd) {
        InputStream i = null;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            i = p.getInputStream();
            StringBuilder sb = new StringBuilder();
            for (int ch; (ch = i.read()) != -1; ) {
                sb.append((char) ch);
            }
            return sb.toString();
        } catch (Exception e) {
            if (i != null) {
                try {
                    i.close();
                } catch (IOException ex) {
                    log.error(ex.getMessage());
                }
            }
        }

        return null;
    }
}
