package tn.esprit.autoloc.autolocapi.service.impl;

import tn.esprit.autoloc.autolocapi.domain.Contrat;
import tn.esprit.autoloc.autolocapi.domain.Reservation;
import tn.esprit.autoloc.autolocapi.exception.BusinessRuleException;
import tn.esprit.autoloc.autolocapi.exception.ConflictException;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.ContratRepository;
import tn.esprit.autoloc.autolocapi.repository.ReservationRepository;
import tn.esprit.autoloc.autolocapi.service.IContratService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratServiceImpl implements IContratService {

    private final ContratRepository contratRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public Contrat create(Contrat contrat) {
        verifierMontant(contrat);
        contrat.setIdContrat(null);
        if (contrat.getDateSignature() == null) {
            contrat.setDateSignature(LocalDate.now());
        }
        if (contrat.getValide() == null) {
            contrat.setValide(false);
        }
        contrat.setReservation(resolveReservation(contrat.getReservation(), null));
        return contratRepository.save(contrat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Contrat findById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
    }

    @Override
    public Contrat update(Long id, Contrat data) {
        Contrat existing = findById(id);
        verifierMontant(data);
        if (data.getDateSignature() != null) {
            existing.setDateSignature(data.getDateSignature());
        }
        existing.setMontantTotal(data.getMontantTotal());
        if (data.getValide() != null) {
            existing.setValide(data.getValide());
        }
        existing.setReservation(resolveReservation(data.getReservation(), id));
        return contratRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        contratRepository.delete(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contrat> findByValide(Boolean valide) {
        return contratRepository.findByValide(valide);
    }

    @Override
    @Transactional(readOnly = true)
    public Contrat findByReservation(Long idReservation) {
        return contratRepository.findByReservationIdReservation(idReservation)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aucun contrat trouvé pour la réservation " + idReservation));
    }

    // ---------- helpers ----------

    private void verifierMontant(Contrat c) {
        if (c.getMontantTotal() == null || c.getMontantTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Le montant total du contrat doit être strictement positif");
        }
    }

    /** Une réservation ne peut avoir qu'un seul contrat (relation OneToOne). */
    private Reservation resolveReservation(Reservation ref, Long contratCourantId) {
        if (ref == null || ref.getIdReservation() == null) {
            return null;
        }
        Reservation reservation = reservationRepository.findById(ref.getIdReservation())
                .orElseThrow(() -> new ResourceNotFoundException("Réservation", ref.getIdReservation()));
        contratRepository.findByReservationIdReservation(reservation.getIdReservation())
                .filter(autre -> !autre.getIdContrat().equals(contratCourantId))
                .ifPresent(autre -> {
                    throw new ConflictException("La réservation " + reservation.getIdReservation()
                            + " possède déjà le contrat " + autre.getIdContrat());
                });
        return reservation;
    }
}
