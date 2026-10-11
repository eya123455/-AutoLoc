package tn.esprit.autoloc.autolocapi.repository;

import tn.esprit.autoloc.autolocapi.domain.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    List<Maintenance> findByVehiculeIdVehicule(Long idVehicule);

    /** Maintenances encore en cours (pas de date de fin). */
    List<Maintenance> findByDateFinIsNull();
}
