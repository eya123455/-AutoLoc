package tn.esprit.autoloc.autolocapi.web.mapper;

import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.web.dto.AgenceRequest;
import tn.esprit.autoloc.autolocapi.web.dto.AgenceResponse;
import org.springframework.stereotype.Component;

@Component
public class AgenceMapper {

    public Agence toEntity(AgenceRequest request) {
        Agence agence = new Agence();
        agence.setNom(request.nom().trim());
        agence.setVille(request.ville().trim());
        agence.setAdresse(request.adresse().trim());
        agence.setTelephone(request.telephone());
        return agence;
    }

    public AgenceResponse toResponse(Agence agence) {
        return new AgenceResponse(
                agence.getIdAgence(),
                agence.getNom(),
                agence.getVille(),
                agence.getAdresse(),
                agence.getTelephone());
    }
}
