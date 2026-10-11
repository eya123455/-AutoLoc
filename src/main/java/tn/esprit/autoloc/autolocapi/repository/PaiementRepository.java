package tn.esprit.autoloc.autolocapi.repository;

import tn.esprit.autoloc.autolocapi.domain.ModePaiement;
import tn.esprit.autoloc.autolocapi.domain.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    List<Paiement> findByContratIdContrat(Long idContrat);

    List<Paiement> findByModePaiement(ModePaiement modePaiement);

    /** Renvoie null s'il n'y a aucun paiement pour ce contrat. */
    @Query("select sum(p.montant) from Paiement p where p.contrat.idContrat = :idContrat")
    BigDecimal sumMontantByContrat(@Param("idContrat") Long idContrat);
}
