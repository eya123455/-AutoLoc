package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.service.IPaiementService;
import java.util.List;

@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final IPaiementService paiementService;

    @GetMapping
    public List<Paiement> retrieveAllPaiements() {
        return paiementService.retrieveAllPaiements();
    }

    @PostMapping
    public Paiement addPaiement(@RequestBody Paiement p) {
        return paiementService.addPaiement(p);
    }

    @PutMapping
    public Paiement updatePaiement(@RequestBody Paiement p) {
        return paiementService.updatePaiement(p);
    }

    @GetMapping("/{id}")
    public Paiement retrievePaiement(@PathVariable Long id) {
        return paiementService.retrievePaiement(id);
    }

    @DeleteMapping("/{id}")
    public void removePaiement(@PathVariable Long id) {
        paiementService.removePaiement(id);
    }
}
