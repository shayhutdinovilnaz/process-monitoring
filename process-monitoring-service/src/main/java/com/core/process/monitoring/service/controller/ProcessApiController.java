package com.core.process.monitoring.service.controller;

import com.core.api.ProcessApi;
import com.core.models.Process;
import com.core.process.monitoring.service.facade.ProcessFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class ProcessApiController implements ProcessApi {
    private final ProcessFacade processFacade;

    @Override
    public ResponseEntity<List<Process>> getAllProcess() {
        return ResponseEntity.ok(processFacade.getAll());
    }

    @Override
    public ResponseEntity<Void> saveAll(List<Process> processes) {
         processFacade.saveAll(processes);
         return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<Void> switchProcessRefresh(Boolean switcherVar) {
        processFacade.switchStateOfProcessesRefresh(switcherVar);
        return ResponseEntity.ok(null);
    }
}
