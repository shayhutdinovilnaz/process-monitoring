package com.core.process.monitoring.service.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class ProcessModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private int pid;
    @Column
    private String user;
    @Column
    private String usedRAM;
    @Column
    private String usedCPU;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentProcess", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<ProcessModel> children;
    @ManyToOne
    @JoinColumn
    private ProcessModel parentProcess;
}
