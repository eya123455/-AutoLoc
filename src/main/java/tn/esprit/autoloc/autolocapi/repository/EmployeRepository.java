package tn.esprit.autoloc.autolocapi.repository;

import tn.esprit.autoloc.autolocapi.domain.Employe;
import tn.esprit.autoloc.autolocapi.domain.RoleEmploye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {

    List<Employe> findByAgenceIdAgence(Long idAgence);

    List<Employe> findByRole(RoleEmploye role);
}
