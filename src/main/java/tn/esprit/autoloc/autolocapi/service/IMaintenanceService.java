package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.domain.Maintenance;

import java.util.List;

public interface IMaintenanceService {

    Maintenance create(Maintenance maintenance);

    List<Maintenance> findAll();

    Maintenance findById(Long id);

    Maintenance update(Long id, Maintenance maintenance);

    void delete(Long id);

    List<Maintenance> findByVehicule(Long idVehicule);

    /** Maintenances sans date de fin. */
    List<Maintenance> findEnCours();
}
