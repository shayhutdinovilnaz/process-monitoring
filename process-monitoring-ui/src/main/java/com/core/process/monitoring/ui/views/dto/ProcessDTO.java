package com.core.process.monitoring.ui.views.dto;

import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class ProcessDTO {
    private Integer id;
    private Integer pid;
    private String user;
    private String usedCPU;
    private String usedRAM;
    private List<ProcessDTO> children;
}
