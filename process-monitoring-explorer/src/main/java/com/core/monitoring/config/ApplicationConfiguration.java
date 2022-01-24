package com.core.monitoring.config;

import com.core.monitoring.service.OperationSystemInfoService;
import com.core.monitoring.service.impl.UnixOperationSystemInfoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public Map<String, OperationSystemInfoService> operationSystemInfoServices() {
        final Map<String, OperationSystemInfoService> maps = new HashMap<>();
        maps.put("unix", new UnixOperationSystemInfoService());
        return maps;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
