package tn.esprit.autoloc.autolocapi.service.impl;

import tn.esprit.autoloc.autolocapi.domain.Equipement;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.EquipementRepository;
import tn.esprit.autoloc.autolocapi.service.IEquipementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipementServiceImpl implements IEquipementService {

    private final EquipementRepository equipementRepository;

    @Override
    public Equipement create(Equipement equipement) {
        equipement.setIdEquipement(null);
        return equipementRepository.save(equipement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipement> findAll() {
        return equipementRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Equipement findById(Long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Équipement", id));
    }

    @Override
    public Equipement update(Long id, Equipement data) {
        Equipement existing = findById(id);
        existing.setLibelle(data.getLibelle());
        return equipementRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Equipement equipement = findById(id);
        // Equipement est le côté "inverse" du ManyToMany : on détache d'abord
        // les véhicules, sinon la ligne de la table vehicule_equipement bloque la suppression.
        for (Vehicule vehicule : equipement.getVehicules()) {
            vehicule.getEquipements().remove(equipement);
        }
        equipementRepository.delete(equipement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipement> search(String libelle) {
        return equipementRepository.findByLibelleContainingIgnoreCase(libelle);
    }
}
