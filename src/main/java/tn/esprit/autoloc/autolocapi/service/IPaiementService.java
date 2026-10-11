package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.domain.ModePaiement;
import tn.esprit.autoloc.autolocapi.domain.Paiement;

import java.math.BigDecimal;
import java.util.List;

public interface IPaiementService {

    Paiement create(Paiement paiement);

    List<Paiement> findAll();

    Paiement findById(Long id);

    Paiement update(Long id, Paiement paiement);

    void delete(Long id);

    List<Paiement> findByContrat(Long idContrat);

    List<Paiement> findByMode(ModePaiement mode);

    /** Somme des paiements déjà enregistrés pour un contrat. */
    BigDecimal getTotalPaye(Long idContrat);
}
