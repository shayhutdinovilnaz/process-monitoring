package com.core.process.monitoring.service.converter.impl;

import com.core.models.Process;
import com.core.process.monitoring.service.model.ProcessModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class ProcessConverterTest {

    private final ProcessConverter underTest = new ProcessConverter();

    @Test(expected = NullPointerException.class)
    public void convert_nullableSource_exception() {
        underTest.convert(null);
    }

    @Test
    public void convert_validSource_success() {
        final String userName = "userName";
        final String usedRAM = "usedRAM";
        final String usedCPU = "usedCPU";
        final int pid = 1;

        final ProcessModel childProcess = new ProcessModel();
        childProcess.setUser(userName);
        childProcess.setUsedRAM(usedRAM);
        childProcess.setUsedCPU(usedCPU);
        childProcess.setPid(pid);

        final ProcessModel parentProcess = new ProcessModel();
        parentProcess.setUser(userName);
        parentProcess.setUsedRAM(usedRAM);
        parentProcess.setUsedCPU(usedCPU);
        parentProcess.setPid(pid);
        parentProcess.setChildren(Collections.singletonList(childProcess));

        final Process actual = underTest.convert(parentProcess);
        Assert.assertEquals(actual.getPid(), parentProcess.getPid(), 0);
        Assert.assertEquals(actual.getUser(), parentProcess.getUser());
        Assert.assertEquals(actual.getUsedCPU(), parentProcess.getUsedCPU());
        Assert.assertEquals(actual.getUsedRAM(), parentProcess.getUsedRAM());
        Assert.assertEquals(actual.getChildren().size(), parentProcess.getChildren().size());
    }

    @Test(expected = NullPointerException.class)
    public void reverseConvert_nullableSource_exception() {
        underTest.reverseConvert(null);
    }

    @Test
    public void reverseConvert_validSource_success() {
        final String userName = "userName";
        final String usedRAM = "usedRAM";
        final String usedCPU = "usedCPU";
        final int pid = 1;

        final Process childProcess = new Process();
        childProcess.setUser(userName);
        childProcess.setUsedRAM(usedRAM);
        childProcess.setUsedCPU(usedCPU);
        childProcess.setPid(pid);

        final Process parentProcess = new Process();
        parentProcess.setUser(userName);
        parentProcess.setUsedRAM(usedRAM);
        parentProcess.setUsedCPU(usedCPU);
        parentProcess.setPid(pid);
        parentProcess.setChildren(Collections.singletonList(childProcess));

        final ProcessModel actual = underTest.reverseConvert(parentProcess);
        Assert.assertEquals(actual.getPid(), parentProcess.getPid(), 0);
        Assert.assertEquals(actual.getUser(), parentProcess.getUser());
        Assert.assertEquals(actual.getUsedCPU(), parentProcess.getUsedCPU());
        Assert.assertEquals(actual.getUsedRAM(), parentProcess.getUsedRAM());
        Assert.assertEquals(actual.getChildren().size(), parentProcess.getChildren().size());
    }
}