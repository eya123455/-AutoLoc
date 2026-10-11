package tn.esprit.autoloc.autolocapi.web.dto;

import tn.esprit.autoloc.autolocapi.domain.CategorieVehicule;
import tn.esprit.autoloc.autolocapi.domain.StatutVehicule;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record VehiculeRequest(
        @NotBlank(message = "L'immatriculation est obligatoire")
        @Size(max = 20, message = "L'immatriculation ne doit pas dépasser 20 caractères")
        String immatriculation,

        @NotBlank(message = "La marque est obligatoire")
        @Size(max = 50, message = "La marque ne doit pas dépasser 50 caractères")
        String marque,

        @NotBlank(message = "Le modèle est obligatoire")
        @Size(max = 50, message = "Le modèle ne doit pas dépasser 50 caractères")
        String modele,

        @NotNull(message = "La catégorie est obligatoire")
        CategorieVehicule categorie,

        @NotNull(message = "Le tarif journalier est obligatoire")
        @Positive(message = "Le tarif journalier doit être strictement positif")
        @Digits(integer = 8, fraction = 2, message = "Le tarif doit avoir au plus 8 chiffres et 2 décimales")
        BigDecimal tarifJournalier,

        /** Optionnel : DISPONIBLE par défaut à la création. */
        StatutVehicule statut,

        @NotNull(message = "L'agence est obligatoire")
        Long agenceId,

        /** Optionnel : ids d'équipements existants. */
        List<Long> equipementIds
) {
}
