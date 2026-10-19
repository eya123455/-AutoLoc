package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.service.IContratService;
import java.util.List;

@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
public class ContratController {

    private final IContratService contratService;

    @GetMapping
    public List<Contrat> retrieveAllContrats() {
        return contratService.retrieveAllContrats();
    }

    @PostMapping
    public Contrat addContrat(@RequestBody Contrat c) {
        return contratService.addContrat(c);
    }

    @PutMapping
    public Contrat updateContrat(@RequestBody Contrat c) {
        return contratService.updateContrat(c);
    }

    @GetMapping("/{id}")
    public Contrat retrieveContrat(@PathVariable Long id) {
        return contratService.retrieveContrat(id);
    }

    @DeleteMapping("/{id}")
    public void removeContrat(@PathVariable Long id) {
        contratService.removeContrat(id);
    }
}
