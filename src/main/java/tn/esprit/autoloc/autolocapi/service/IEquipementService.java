package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.domain.Equipement;

import java.util.List;

public interface IEquipementService {

    Equipement create(Equipement equipement);

    List<Equipement> findAll();

    Equipement findById(Long id);

    Equipement update(Long id, Equipement equipement);

    void delete(Long id);

    List<Equipement> search(String libelle);
}
