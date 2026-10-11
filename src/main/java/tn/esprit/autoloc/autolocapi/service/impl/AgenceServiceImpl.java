package tn.esprit.autoloc.autolocapi.service.impl;

import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.AgenceRepository;
import tn.esprit.autoloc.autolocapi.service.IAgenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AgenceServiceImpl implements IAgenceService {

    private final AgenceRepository agenceRepository;

    @Override
    public Agence create(Agence agence) {
        agence.setIdAgence(null); // l'id est toujours généré par la base
        return agenceRepository.save(agence);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agence> findAll() {
        return agenceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Agence findById(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
    }

    @Override
    public Agence update(Long id, Agence data) {
        Agence existing = findById(id);
        existing.setNom(data.getNom());
        existing.setVille(data.getVille());
        existing.setAdresse(data.getAdresse());
        existing.setTelephone(data.getTelephone());
        return agenceRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        agenceRepository.delete(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agence> findByVille(String ville) {
        return agenceRepository.findByVilleIgnoreCase(ville);
    }
}
