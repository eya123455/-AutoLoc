package tn.esprit.autoloc.autolocapi.web.dto;

import tn.esprit.autoloc.autolocapi.domain.CategorieVehicule;
import tn.esprit.autoloc.autolocapi.domain.StatutVehicule;

import java.math.BigDecimal;
import java.util.List;

public record VehiculeResponse(
        Long idVehicule,
        String immatriculation,
        String marque,
        String modele,
        CategorieVehicule categorie,
        BigDecimal tarifJournalier,
        StatutVehicule statut,
        Long agenceId,
        String agenceNom,
        List<EquipementInfo> equipements
) {
    public record EquipementInfo(Long idEquipement, String libelle) {
    }
}
