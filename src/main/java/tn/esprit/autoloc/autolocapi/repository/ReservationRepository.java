package tn.esprit.autoloc.autolocapi.repository;

import tn.esprit.autoloc.autolocapi.domain.Reservation;
import tn.esprit.autoloc.autolocapi.domain.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByClientIdClient(Long idClient);

    List<Reservation> findByVehiculeIdVehicule(Long idVehicule);

    List<Reservation> findByStatut(StatutReservation statut);

    /**
     * Vrai s'il existe déjà une réservation (autre que {@code excludeId}) dont le statut est dans
     * {@code statuts} et dont la période chevauche [debut, fin] pour ce véhicule.
     */
    @Query("""
            select count(r) > 0 from Reservation r
            where r.vehicule.idVehicule = :vehiculeId
              and r.idReservation <> :excludeId
              and r.statut in :statuts
              and r.dateDebut <= :fin
              and r.dateFin >= :debut
            """)
    boolean existsOverlapping(@Param("vehiculeId") Long vehiculeId,
                              @Param("debut") LocalDate debut,
                              @Param("fin") LocalDate fin,
                              @Param("statuts") Collection<StatutReservation> statuts,
                              @Param("excludeId") Long excludeId);
}
