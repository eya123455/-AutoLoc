package tn.esprit.autoloc.autolocapi.service.impl;

import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.domain.Employe;
import tn.esprit.autoloc.autolocapi.domain.RoleEmploye;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.AgenceRepository;
import tn.esprit.autoloc.autolocapi.repository.EmployeRepository;
import tn.esprit.autoloc.autolocapi.service.IEmployeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeServiceImpl implements IEmployeService {

    private final EmployeRepository employeRepository;
    private final AgenceRepository agenceRepository;

    @Override
    public Employe create(Employe employe) {
        employe.setIdEmploye(null);
        employe.setAgence(resolveAgence(employe.getAgence()));
        return employeRepository.save(employe);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Employe findById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé", id));
    }

    @Override
    public Employe update(Long id, Employe data) {
        Employe existing = findById(id);
        existing.setNom(data.getNom());
        existing.setPrenom(data.getPrenom());
        existing.setRole(data.getRole());
        existing.setAgence(resolveAgence(data.getAgence()));
        return employeRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        employeRepository.delete(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employe> findByAgence(Long idAgence) {
        return employeRepository.findByAgenceIdAgence(idAgence);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employe> findByRole(RoleEmploye role) {
        return employeRepository.findByRole(role);
    }

    /** Remplace une référence (id seul) par l'agence réellement chargée en base. */
    private Agence resolveAgence(Agence ref) {
        if (ref == null || ref.getIdAgence() == null) {
            return null;
        }
        return agenceRepository.findById(ref.getIdAgence())
                .orElseThrow(() -> new ResourceNotFoundException("Agence", ref.getIdAgence()));
    }
}
