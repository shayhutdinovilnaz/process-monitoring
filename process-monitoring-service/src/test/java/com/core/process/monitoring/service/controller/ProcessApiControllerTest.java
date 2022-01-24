package com.core.process.monitoring.service.controller;

import com.core.models.Process;
import com.core.process.monitoring.service.facade.ProcessFacade;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@WebMvcTest()
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class ProcessApiControllerTest {
    @Autowired
    protected MockMvc mvc;

    @MockBean
    private ProcessFacade processFacade;

    @Test
    public void getAllProcess_success() throws Exception {
        final String url = "/api/v1/process";

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

        when(processFacade.getAll()).thenReturn(Collections.singletonList(parentProcess));

        final String responseBody = mvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        Assert.assertEquals(1, parseFromJson(responseBody, ArrayList.class).size());
        verify(processFacade).getAll();
        verifyNoMoreInteractions(processFacade);
    }

    @Test
    public void saveAll_success() throws Exception {
        final String url = "/api/v1/process/saveAll";

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

        mvc.perform(MockMvcRequestBuilders.post(url)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(parseToJson(Collections.singletonList(parentProcess))))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        verify(processFacade).saveAll(any());
        verifyNoMoreInteractions(processFacade);
    }

    @Test
    public void switchProcessRefresh_switchToEnable_sucess() throws Exception {
        final String url = "/api/v1/process/switch/true";

        mvc.perform(MockMvcRequestBuilders.put(url))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        verify(processFacade).switchStateOfProcessesRefresh(true);
        verifyNoMoreInteractions(processFacade);
    }

    protected <T> T parseFromJson(final String json, final Class<T> clazz) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }

    protected String parseToJson(final Object obj) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}