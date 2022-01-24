package com.core.process.monitoring.service.facade;


import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Data
@Component
public class ProcessRefreshContext {
    private AtomicBoolean refreshEnable = new AtomicBoolean(true);
}
