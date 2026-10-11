package tn.esprit.autoloc.autolocapi.web.controller;

import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.service.IAgenceService;
import tn.esprit.autoloc.autolocapi.web.dto.AgenceRequest;
import tn.esprit.autoloc.autolocapi.web.dto.AgenceResponse;
import tn.esprit.autoloc.autolocapi.web.mapper.AgenceMapper;
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
@RequestMapping("/api/agences")
@RequiredArgsConstructor
public class AgenceController {

    private final IAgenceService agenceService;
    private final AgenceMapper agenceMapper;

    /** POST /api/agences : crée une agence (201 + header Location). */
    @PostMapping
    public ResponseEntity<AgenceResponse> create(@Valid @RequestBody AgenceRequest request) {
        Agence created = agenceService.create(agenceMapper.toEntity(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getIdAgence())
                .toUri();
        return ResponseEntity.created(location).body(agenceMapper.toResponse(created));
    }

    /** GET /api/agences  ou  GET /api/agences?ville=Tunis */
    @GetMapping
    public List<AgenceResponse> findAll(@RequestParam(required = false) String ville) {
        List<Agence> agences = (ville == null || ville.isBlank())
                ? agenceService.findAll()
                : agenceService.findByVille(ville.trim());
        return agences.stream().map(agenceMapper::toResponse).toList();
    }

    /** GET /api/agences/{id} */
    @GetMapping("/{id}")
    public AgenceResponse findById(@PathVariable Long id) {
        return agenceMapper.toResponse(agenceService.findById(id));
    }

    /** PUT /api/agences/{id} : remplace les données de l'agence. */
    @PutMapping("/{id}")
    public AgenceResponse update(@PathVariable Long id, @Valid @RequestBody AgenceRequest request) {
        return agenceMapper.toResponse(agenceService.update(id, agenceMapper.toEntity(request)));
    }

    /** DELETE /api/agences/{id} : 204, ou 409 si l'agence possède encore des véhicules/employés. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        agenceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
