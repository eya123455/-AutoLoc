package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.domain.Agence;

import java.util.List;

public interface IAgenceService {

    Agence create(Agence agence);

    List<Agence> findAll();

    Agence findById(Long id);

    Agence update(Long id, Agence agence);

    void delete(Long id);

    List<Agence> findByVille(String ville);
}
