package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.domain.Contrat;

import java.util.List;

public interface IContratService {

    Contrat create(Contrat contrat);

    List<Contrat> findAll();

    Contrat findById(Long id);

    Contrat update(Long id, Contrat contrat);

    void delete(Long id);

    List<Contrat> findByValide(Boolean valide);

    Contrat findByReservation(Long idReservation);
}
