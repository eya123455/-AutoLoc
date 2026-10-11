package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.domain.Employe;
import tn.esprit.autoloc.autolocapi.domain.RoleEmploye;

import java.util.List;

public interface IEmployeService {

    Employe create(Employe employe);

    List<Employe> findAll();

    Employe findById(Long id);

    Employe update(Long id, Employe employe);

    void delete(Long id);

    List<Employe> findByAgence(Long idAgence);

    List<Employe> findByRole(RoleEmploye role);
}
