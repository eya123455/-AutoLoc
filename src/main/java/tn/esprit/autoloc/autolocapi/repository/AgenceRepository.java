package tn.esprit.autoloc.autolocapi.repository;

import tn.esprit.autoloc.autolocapi.domain.Agence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgenceRepository extends JpaRepository<Agence, Long> {

    List<Agence> findByVilleIgnoreCase(String ville);
}
