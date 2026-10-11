package tn.esprit.autoloc.autolocapi.service.impl;

import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.domain.CategorieVehicule;
import tn.esprit.autoloc.autolocapi.domain.Equipement;
import tn.esprit.autoloc.autolocapi.domain.StatutVehicule;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import tn.esprit.autoloc.autolocapi.exception.ConflictException;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.AgenceRepository;
import tn.esprit.autoloc.autolocapi.repository.EquipementRepository;
import tn.esprit.autoloc.autolocapi.repository.VehiculeRepository;
import tn.esprit.autoloc.autolocapi.service.IVehiculeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class VehiculeServiceImpl implements IVehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final AgenceRepository agenceRepository;
    private final EquipementRepository equipementRepository;

    @Override
    public Vehicule create(Vehicule vehicule) {
        if (vehiculeRepository.existsByImmatriculation(vehicule.getImmatriculation())) {
            throw new ConflictException(
                    "Un véhicule avec l'immatriculation '" + vehicule.getImmatriculation() + "' existe déjà");
        }
        vehicule.setIdVehicule(null);
        if (vehicule.getStatut() == null) {
            vehicule.setStatut(StatutVehicule.DISPONIBLE);
        }
        vehicule.setAgence(resolveAgence(vehicule.getAgence()));
        vehicule.setEquipements(resolveEquipements(vehicule.getEquipements()));
        return vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> findAll() {
        return vehiculeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicule findById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule", id));
    }

    @Override
    public Vehicule update(Long id, Vehicule data) {
        Vehicule existing = findById(id);
        if (vehiculeRepository.existsByImmatriculationAndIdVehiculeNot(data.getImmatriculation(), id)) {
            throw new ConflictException(
                    "Un autre véhicule utilise déjà l'immatriculation '" + data.getImmatriculation() + "'");
        }
        existing.setImmatriculation(data.getImmatriculation());
        existing.setMarque(data.getMarque());
        existing.setModele(data.getModele());
        existing.setCategorie(data.getCategorie());
        existing.setTarifJournalier(data.getTarifJournalier());
        if (data.getStatut() != null) {
            existing.setStatut(data.getStatut());
        }
        existing.setAgence(resolveAgence(data.getAgence()));
        existing.setEquipements(resolveEquipements(data.getEquipements()));
        return vehiculeRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        vehiculeRepository.delete(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> findByStatut(StatutVehicule statut) {
        return vehiculeRepository.findByStatut(statut);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> findByCategorie(CategorieVehicule categorie) {
        return vehiculeRepository.findByCategorie(categorie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> findByAgence(Long idAgence) {
        return vehiculeRepository.findByAgenceIdAgence(idAgence);
    }

    // ---------- helpers ----------

    private Agence resolveAgence(Agence ref) {
        if (ref == null || ref.getIdAgence() == null) {
            return null;
        }
        return agenceRepository.findById(ref.getIdAgence())
                .orElseThrow(() -> new ResourceNotFoundException("Agence", ref.getIdAgence()));
    }

    private List<Equipement> resolveEquipements(List<Equipement> refs) {
        if (refs == null || refs.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> ids = refs.stream()
                .map(Equipement::getIdEquipement)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        List<Equipement> found = equipementRepository.findAllById(ids);
        if (found.size() != ids.size()) {
            Long manquant = ids.stream()
                    .filter(i -> found.stream().noneMatch(e -> e.getIdEquipement().equals(i)))
                    .findFirst()
                    .orElse(null);
            throw new ResourceNotFoundException("Équipement", manquant);
        }
        return new ArrayList<>(found);
    }
}
