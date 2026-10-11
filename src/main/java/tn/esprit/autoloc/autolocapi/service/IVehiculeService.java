package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.domain.CategorieVehicule;
import tn.esprit.autoloc.autolocapi.domain.StatutVehicule;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;

import java.util.List;

public interface IVehiculeService {

    Vehicule create(Vehicule vehicule);

    List<Vehicule> findAll();

    Vehicule findById(Long id);

    Vehicule update(Long id, Vehicule vehicule);

    void delete(Long id);

    List<Vehicule> findByStatut(StatutVehicule statut);

    List<Vehicule> findByCategorie(CategorieVehicule categorie);

    List<Vehicule> findByAgence(Long idAgence);
}
