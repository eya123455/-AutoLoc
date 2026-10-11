package tn.esprit.autoloc.autolocapi.repository;

import tn.esprit.autoloc.autolocapi.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNumPermis(String numPermis);

    boolean existsByEmailAndIdClientNot(String email, Long idClient);

    boolean existsByNumPermisAndIdClientNot(String numPermis, Long idClient);

    List<Client> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
}
