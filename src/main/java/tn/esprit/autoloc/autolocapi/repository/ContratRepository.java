package tn.esprit.autoloc.autolocapi.repository;

import tn.esprit.autoloc.autolocapi.domain.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContratRepository extends JpaRepository<Contrat, Long> {

    List<Contrat> findByValide(Boolean valide);

    Optional<Contrat> findByReservationIdReservation(Long idReservation);
}
