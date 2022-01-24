package com.core.process.monitoring.ui.views.list;

import com.core.models.Process;
import com.core.process.monitoring.ui.views.MainLayout;
import com.core.process.monitoring.ui.views.dto.ProcessDTO;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@PageTitle("List")
@Route(value = "list", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@Slf4j
public class ListView extends HorizontalLayout {
    private static final String URL_PROCESS_SERVICE_GET = "/api/v1/process";
    private static final String URL_PROCESS_SERVICE_SWITCH_UPDATE = "/api/v1/process/switch/";
    private static final String BASE_PATH = "http://process-monitoring-service:8080";
    private static final int MAX_ATTEMPTS_COUNT = 3;
    private static boolean enableRefresh = true;

    private final TreeGrid<ProcessDTO> treeGrid;
    private final RestTemplate restTemplate;

    public ListView() {
        restTemplate = new RestTemplate();
        treeGrid = createTreeGrid();
        final Button button = createRefreshButton();
        final RadioButtonGroup<Boolean> radioGroup = creteRadioButton();
        final VerticalLayout verticalLayout = createVerticalLayout(treeGrid, button, radioGroup);

        setMargin(true);
        add(verticalLayout);
    }

    private TreeGrid<ProcessDTO> createTreeGrid() {
        final TreeGrid<ProcessDTO> grid = new TreeGrid<>();
        grid.setItems(getProcesses(), this::getChildrenForProcess);
        grid.addHierarchyColumn(ProcessDTO::getPid).setHeader("PID").setSortable(true);
        grid.addColumn(ProcessDTO::getUser).setHeader("USER").setSortable(true);
        grid.addColumn(ProcessDTO::getUsedRAM).setHeader("USED RAM").setSortable(true);
        grid.addColumn(ProcessDTO::getUsedCPU).setHeader("USED CPU").setSortable(true);
        return grid;
    }

    private List<ProcessDTO> getProcesses() {
        ResponseEntity<Process[]> responseEntity = null;
        int count = 0;
        do {
            try {
                count++;
                responseEntity = restTemplate.getForEntity(BASE_PATH + URL_PROCESS_SERVICE_GET, Process[].class);
            } catch (Exception e) {
                log.error("Error during execute request.", e);
            }
        } while (count <= MAX_ATTEMPTS_COUNT && !HttpStatus.OK.equals(Optional.ofNullable(responseEntity).map(ResponseEntity::getStatusCode).orElse(null)));

        log.info("List of processes are received.");
        return Optional.ofNullable(responseEntity).map(HttpEntity::getBody)
                .stream()
                .flatMap(Stream::of)
                .map(this::convert)
                .collect(Collectors.toList());
    }

    private ProcessDTO convert(Process process) {
        final List<ProcessDTO> children = Optional.ofNullable(process.getChildren()).orElseGet(ArrayList::new).stream().map(this::convert).collect(Collectors.toList());
        return ProcessDTO.builder()
                .id(RandomUtils.nextInt())
                .user(process.getUser())
                .pid(process.getPid())
                .usedCPU(process.getUsedCPU())
                .usedRAM(process.getUsedRAM())
                .children(children)
                .build();
    }

    public List<ProcessDTO> getChildrenForProcess(ProcessDTO p) {
        return Optional.ofNullable(p).map(ProcessDTO::getChildren).orElseGet(ArrayList::new);
    }


    private Button createRefreshButton() {
        final Button button = new Button("Refresh", new Icon(VaadinIcon.REFRESH));
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> treeGrid.setItems(getProcesses(), this::getChildrenForProcess));
        return button;
    }

    private RadioButtonGroup<Boolean> creteRadioButton() {
        final RadioButtonGroup<Boolean> radioGroup = new RadioButtonGroup<>();
        radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        radioGroup.setLabel("Enable updating of processes list");
        radioGroup.setItems(true, false);
        radioGroup.setValue(enableRefresh);
        switchUpdateOfProcess(enableRefresh);
        radioGroup.addValueChangeListener(x -> switchUpdateOfProcess(x.getValue()));
        return radioGroup;
    }

    private void switchUpdateOfProcess(boolean switchValue) {
        enableRefresh = switchValue;
        int count = 0;
        do {
            try {
                count++;
                restTemplate.exchange(BASE_PATH + URL_PROCESS_SERVICE_SWITCH_UPDATE + switchValue, HttpMethod.PUT, HttpEntity.EMPTY, Void.class);
                log.info("Request for switching update mode is executed. Value: {}", switchValue);
                return;
            } catch (Exception e) {
                log.error("Error during execute request.", e);
            }
        } while (count <= MAX_ATTEMPTS_COUNT);
    }

    private VerticalLayout createVerticalLayout(TreeGrid<ProcessDTO> treeGrid, Button button, RadioButtonGroup<Boolean> radioGroup) {
        final VerticalLayout l = new VerticalLayout();
        l.add(radioGroup);
        l.add(button);
        l.add(treeGrid);
        return l;
    }
}
