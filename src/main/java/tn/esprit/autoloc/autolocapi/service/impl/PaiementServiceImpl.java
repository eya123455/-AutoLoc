package tn.esprit.autoloc.autolocapi.service.impl;

import tn.esprit.autoloc.autolocapi.domain.Contrat;
import tn.esprit.autoloc.autolocapi.domain.ModePaiement;
import tn.esprit.autoloc.autolocapi.domain.Paiement;
import tn.esprit.autoloc.autolocapi.exception.BusinessRuleException;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.ContratRepository;
import tn.esprit.autoloc.autolocapi.repository.PaiementRepository;
import tn.esprit.autoloc.autolocapi.service.IPaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaiementServiceImpl implements IPaiementService {

    private final PaiementRepository paiementRepository;
    private final ContratRepository contratRepository;

    @Override
    public Paiement create(Paiement paiement) {
        verifierChamps(paiement);
        paiement.setIdPaiement(null);
        if (paiement.getDatePaiement() == null) {
            paiement.setDatePaiement(LocalDate.now());
        }
        Contrat contrat = resolveContrat(paiement.getContrat());
        verifierPlafond(contrat, paiement.getMontant(), BigDecimal.ZERO);
        paiement.setContrat(contrat);
        return paiementRepository.save(paiement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Paiement findById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement", id));
    }

    @Override
    public Paiement update(Long id, Paiement data) {
        Paiement existing = findById(id);
        verifierChamps(data);
        Contrat contrat = resolveContrat(data.getContrat());
        // on retire l'ancien montant du total seulement s'il comptait pour le même contrat
        BigDecimal ancienMontant = existing.getContrat() != null
                && existing.getContrat().getIdContrat().equals(contrat.getIdContrat())
                ? existing.getMontant() : BigDecimal.ZERO;
        verifierPlafond(contrat, data.getMontant(), ancienMontant);
        existing.setMontant(data.getMontant());
        if (data.getDatePaiement() != null) {
            existing.setDatePaiement(data.getDatePaiement());
        }
        existing.setModePaiement(data.getModePaiement());
        existing.setContrat(contrat);
        return paiementRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        paiementRepository.delete(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paiement> findByContrat(Long idContrat) {
        return paiementRepository.findByContratIdContrat(idContrat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paiement> findByMode(ModePaiement mode) {
        return paiementRepository.findByModePaiement(mode);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalPaye(Long idContrat) {
        BigDecimal total = paiementRepository.sumMontantByContrat(idContrat);
        return total != null ? total : BigDecimal.ZERO;
    }

    // ---------- helpers ----------

    private void verifierChamps(Paiement p) {
        if (p.getMontant() == null || p.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Le montant du paiement doit être strictement positif");
        }
        if (p.getModePaiement() == null) {
            throw new BusinessRuleException("Le mode de paiement est obligatoire");
        }
    }

    /** Le cumul des paiements ne doit pas dépasser le montant total du contrat. */
    private void verifierPlafond(Contrat contrat, BigDecimal nouveauMontant, BigDecimal montantARetirer) {
        BigDecimal dejaPaye = getTotalPaye(contrat.getIdContrat());
        BigDecimal total = dejaPaye.subtract(montantARetirer).add(nouveauMontant);
        if (total.compareTo(contrat.getMontantTotal()) > 0) {
            throw new BusinessRuleException("Le total des paiements (" + total
                    + ") dépasserait le montant du contrat (" + contrat.getMontantTotal() + ")");
        }
    }

    private Contrat resolveContrat(Contrat ref) {
        if (ref == null || ref.getIdContrat() == null) {
            throw new BusinessRuleException("Le contrat du paiement est obligatoire");
        }
        return contratRepository.findById(ref.getIdContrat())
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", ref.getIdContrat()));
    }
}
