package com.core.monitoring.model;

import lombok.Data;

import java.util.List;

@Data
public class ProcessModel {
    private long pid;
    private String user;
    private String usedRAM;
    private String usedCPU;
    private List<ProcessModel> children;
}
