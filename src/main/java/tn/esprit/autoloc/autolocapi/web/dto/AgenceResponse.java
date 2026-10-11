package tn.esprit.autoloc.autolocapi.web.dto;

public record AgenceResponse(
        Long idAgence,
        String nom,
        String ville,
        String adresse,
        String telephone
) {
}
