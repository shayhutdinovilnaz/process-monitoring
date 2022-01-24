package com.core.process.monitoring.service.repository;

import com.core.process.monitoring.service.model.ProcessModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessRepository extends JpaRepository<ProcessModel, Integer> {
    void deleteAllByParentProcessIsNull();
}
