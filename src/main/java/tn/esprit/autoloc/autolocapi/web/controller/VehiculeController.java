package tn.esprit.autoloc.autolocapi.web.controller;

import tn.esprit.autoloc.autolocapi.domain.CategorieVehicule;
import tn.esprit.autoloc.autolocapi.domain.StatutVehicule;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import tn.esprit.autoloc.autolocapi.service.IVehiculeService;
import tn.esprit.autoloc.autolocapi.web.dto.VehiculeRequest;
import tn.esprit.autoloc.autolocapi.web.dto.VehiculeResponse;
import tn.esprit.autoloc.autolocapi.web.mapper.VehiculeMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final IVehiculeService vehiculeService;
    private final VehiculeMapper vehiculeMapper;

    /** POST /api/vehicules : crée un véhicule (201 + header Location). */
    @PostMapping
    public ResponseEntity<VehiculeResponse> create(@Valid @RequestBody VehiculeRequest request) {
        Vehicule created = vehiculeService.create(vehiculeMapper.toEntity(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getIdVehicule())
                .toUri();
        return ResponseEntity.created(location).body(vehiculeMapper.toResponse(created));
    }

    /**
     * GET /api/vehicules
     * Filtre optionnel (un seul à la fois, par ordre de priorité) :
     * ?statut=DISPONIBLE | ?categorie=SUV | ?agenceId=1
     */
    @GetMapping
    public List<VehiculeResponse> findAll(@RequestParam(required = false) StatutVehicule statut,
                                          @RequestParam(required = false) CategorieVehicule categorie,
                                          @RequestParam(required = false) Long agenceId) {
        List<Vehicule> vehicules;
        if (statut != null) {
            vehicules = vehiculeService.findByStatut(statut);
        } else if (categorie != null) {
            vehicules = vehiculeService.findByCategorie(categorie);
        } else if (agenceId != null) {
            vehicules = vehiculeService.findByAgence(agenceId);
        } else {
            vehicules = vehiculeService.findAll();
        }
        return vehicules.stream().map(vehiculeMapper::toResponse).toList();
    }

    /** GET /api/vehicules/{id} */
    @GetMapping("/{id}")
    public VehiculeResponse findById(@PathVariable Long id) {
        return vehiculeMapper.toResponse(vehiculeService.findById(id));
    }

    /** PUT /api/vehicules/{id} : remplace les données du véhicule (équipements inclus). */
    @PutMapping("/{id}")
    public VehiculeResponse update(@PathVariable Long id, @Valid @RequestBody VehiculeRequest request) {
        return vehiculeMapper.toResponse(vehiculeService.update(id, vehiculeMapper.toEntity(request)));
    }

    /** DELETE /api/vehicules/{id} : 204, ou 409 si le véhicule a des réservations/maintenances. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehiculeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
