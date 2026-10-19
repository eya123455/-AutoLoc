package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.service.IEquipementService;
import java.util.List;

@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
public class EquipementController {

    private final IEquipementService equipementService;

    @GetMapping
    public List<Equipement> retrieveAllEquipements() {
        return equipementService.retrieveAllEquipements();
    }

    @PostMapping
    public Equipement addEquipement(@RequestBody Equipement e) {
        return equipementService.addEquipement(e);
    }

    @PutMapping
    public Equipement updateEquipement(@RequestBody Equipement e) {
        return equipementService.updateEquipement(e);
    }

    @GetMapping("/{id}")
    public Equipement retrieveEquipement(@PathVariable Long id) {
        return equipementService.retrieveEquipement(id);
    }

    @DeleteMapping("/{id}")
    public void removeEquipement(@PathVariable Long id) {
        equipementService.removeEquipement(id);
    }
}
