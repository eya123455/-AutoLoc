package tn.esprit.autoloc.autolocapi.repository;

import tn.esprit.autoloc.autolocapi.domain.CategorieVehicule;
import tn.esprit.autoloc.autolocapi.domain.StatutVehicule;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Les @EntityGraph chargent l'agence et les équipements en une seule requête,
 * ce qui évite les LazyInitializationException lors de la conversion en DTO.
 */
@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {

    @Override
    @EntityGraph(attributePaths = {"agence", "equipements"})
    List<Vehicule> findAll();

    @Override
    @EntityGraph(attributePaths = {"agence", "equipements"})
    Optional<Vehicule> findById(Long id);

    @EntityGraph(attributePaths = {"agence", "equipements"})
    List<Vehicule> findByStatut(StatutVehicule statut);

    @EntityGraph(attributePaths = {"agence", "equipements"})
    List<Vehicule> findByCategorie(CategorieVehicule categorie);

    @EntityGraph(attributePaths = {"agence", "equipements"})
    List<Vehicule> findByAgenceIdAgence(Long idAgence);

    Optional<Vehicule> findByImmatriculation(String immatriculation);

    boolean existsByImmatriculation(String immatriculation);

    boolean existsByImmatriculationAndIdVehiculeNot(String immatriculation, Long idVehicule);
}
