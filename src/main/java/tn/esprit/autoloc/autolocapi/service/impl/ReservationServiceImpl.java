package tn.esprit.autoloc.autolocapi.service.impl;

import tn.esprit.autoloc.autolocapi.domain.Client;
import tn.esprit.autoloc.autolocapi.domain.Reservation;
import tn.esprit.autoloc.autolocapi.domain.StatutReservation;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import tn.esprit.autoloc.autolocapi.exception.BusinessRuleException;
import tn.esprit.autoloc.autolocapi.exception.ConflictException;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.ClientRepository;
import tn.esprit.autoloc.autolocapi.repository.ReservationRepository;
import tn.esprit.autoloc.autolocapi.repository.VehiculeRepository;
import tn.esprit.autoloc.autolocapi.service.IReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {

    /** Statuts pour lesquels le véhicule est réellement immobilisé. */
    private static final List<StatutReservation> STATUTS_ACTIFS =
            List.of(StatutReservation.EN_ATTENTE, StatutReservation.CONFIRMEE);

    /** Valeur "aucune réservation exclue" pour la requête de chevauchement (les ids sont > 0). */
    private static final Long AUCUN_ID = -1L;

    private final ReservationRepository reservationRepository;
    private final VehiculeRepository vehiculeRepository;
    private final ClientRepository clientRepository;

    @Override
    public Reservation create(Reservation reservation) {
        reservation.setIdReservation(null);
        if (reservation.getStatut() == null) {
            reservation.setStatut(StatutReservation.EN_ATTENTE);
        }
        verifierDates(reservation);
        reservation.setVehicule(resolveVehicule(reservation.getVehicule()));
        reservation.setClient(resolveClient(reservation.getClient()));
        verifierDisponibilite(reservation, AUCUN_ID);
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation", id));
    }

    @Override
    public Reservation update(Long id, Reservation data) {
        Reservation existing = findById(id);
        verifierDates(data);
        existing.setDateDebut(data.getDateDebut());
        existing.setDateFin(data.getDateFin());
        if (data.getStatut() != null) {
            existing.setStatut(data.getStatut());
        }
        existing.setVehicule(resolveVehicule(data.getVehicule()));
        existing.setClient(resolveClient(data.getClient()));
        verifierDisponibilite(existing, id);
        return reservationRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        reservationRepository.delete(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByClient(Long idClient) {
        return reservationRepository.findByClientIdClient(idClient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByVehicule(Long idVehicule) {
        return reservationRepository.findByVehiculeIdVehicule(idVehicule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByStatut(StatutReservation statut) {
        return reservationRepository.findByStatut(statut);
    }

    // ---------- helpers ----------

    private void verifierDates(Reservation r) {
        if (r.getDateDebut() == null || r.getDateFin() == null) {
            throw new BusinessRuleException("Les dates de début et de fin sont obligatoires");
        }
        if (r.getDateFin().isBefore(r.getDateDebut())) {
            throw new BusinessRuleException("La date de fin ne peut pas précéder la date de début");
        }
    }

    private void verifierDisponibilite(Reservation r, Long excludeId) {
        if (!STATUTS_ACTIFS.contains(r.getStatut())) {
            return; // une réservation annulée/terminée n'immobilise pas le véhicule
        }
        boolean chevauche = reservationRepository.existsOverlapping(
                r.getVehicule().getIdVehicule(), r.getDateDebut(), r.getDateFin(), STATUTS_ACTIFS, excludeId);
        if (chevauche) {
            throw new ConflictException("Le véhicule est déjà réservé sur cette période");
        }
    }

    private Vehicule resolveVehicule(Vehicule ref) {
        if (ref == null || ref.getIdVehicule() == null) {
            throw new BusinessRuleException("Le véhicule de la réservation est obligatoire");
        }
        return vehiculeRepository.findById(ref.getIdVehicule())
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule", ref.getIdVehicule()));
    }

    private Client resolveClient(Client ref) {
        if (ref == null || ref.getIdClient() == null) {
            throw new BusinessRuleException("Le client de la réservation est obligatoire");
        }
        return clientRepository.findById(ref.getIdClient())
                .orElseThrow(() -> new ResourceNotFoundException("Client", ref.getIdClient()));
    }
}
