package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.service.IMaintenanceService;
import java.util.List;

@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;

    @GetMapping
    public List<Maintenance> retrieveAllMaintenances() {
        return maintenanceService.retrieveAllMaintenances();
    }

    @PostMapping
    public Maintenance addMaintenance(@RequestBody Maintenance m) {
        return maintenanceService.addMaintenance(m);
    }

    @PutMapping
    public Maintenance updateMaintenance(@RequestBody Maintenance m) {
        return maintenanceService.updateMaintenance(m);
    }

    @GetMapping("/{id}")
    public Maintenance retrieveMaintenance(@PathVariable Long id) {
        return maintenanceService.retrieveMaintenance(id);
    }

    @DeleteMapping("/{id}")
    public void removeMaintenance(@PathVariable Long id) {
        maintenanceService.removeMaintenance(id);
    }
}
