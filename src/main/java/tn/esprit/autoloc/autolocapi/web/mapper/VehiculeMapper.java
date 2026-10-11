package tn.esprit.autoloc.autolocapi.web.mapper;

import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.domain.Equipement;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import tn.esprit.autoloc.autolocapi.web.dto.VehiculeRequest;
import tn.esprit.autoloc.autolocapi.web.dto.VehiculeResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VehiculeMapper {

    /**
     * Construit une entité "légère" : l'agence et les équipements ne portent que leur id.
     * Le service se charge de les remplacer par les entités réelles (et de renvoyer un 404 si absentes).
     */
    public Vehicule toEntity(VehiculeRequest request) {
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation(request.immatriculation().trim());
        vehicule.setMarque(request.marque().trim());
        vehicule.setModele(request.modele().trim());
        vehicule.setCategorie(request.categorie());
        vehicule.setTarifJournalier(request.tarifJournalier());
        vehicule.setStatut(request.statut());

        if (request.agenceId() != null) {
            Agence agence = new Agence();
            agence.setIdAgence(request.agenceId());
            vehicule.setAgence(agence);
        }

        List<Equipement> equipements = new ArrayList<>();
        if (request.equipementIds() != null) {
            for (Long id : request.equipementIds()) {
                Equipement equipement = new Equipement();
                equipement.setIdEquipement(id);
                equipements.add(equipement);
            }
        }
        vehicule.setEquipements(equipements);
        return vehicule;
    }

    public VehiculeResponse toResponse(Vehicule vehicule) {
        Agence agence = vehicule.getAgence();
        List<VehiculeResponse.EquipementInfo> equipements = vehicule.getEquipements().stream()
                .map(e -> new VehiculeResponse.EquipementInfo(e.getIdEquipement(), e.getLibelle()))
                .toList();
        return new VehiculeResponse(
                vehicule.getIdVehicule(),
                vehicule.getImmatriculation(),
                vehicule.getMarque(),
                vehicule.getModele(),
                vehicule.getCategorie(),
                vehicule.getTarifJournalier(),
                vehicule.getStatut(),
                agence != null ? agence.getIdAgence() : null,
                agence != null ? agence.getNom() : null,
                equipements);
    }
}
