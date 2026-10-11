package tn.esprit.autoloc.autolocapi.service.impl;

import tn.esprit.autoloc.autolocapi.domain.Maintenance;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import tn.esprit.autoloc.autolocapi.exception.BusinessRuleException;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.MaintenanceRepository;
import tn.esprit.autoloc.autolocapi.repository.VehiculeRepository;
import tn.esprit.autoloc.autolocapi.service.IMaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehiculeRepository vehiculeRepository;

    @Override
    public Maintenance create(Maintenance maintenance) {
        verifierDates(maintenance);
        maintenance.setIdMaintenance(null);
        maintenance.setVehicule(resolveVehicule(maintenance.getVehicule()));
        return maintenanceRepository.save(maintenance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Maintenance> findAll() {
        return maintenanceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Maintenance findById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance", id));
    }

    @Override
    public Maintenance update(Long id, Maintenance data) {
        Maintenance existing = findById(id);
        verifierDates(data);
        existing.setDateDebut(data.getDateDebut());
        existing.setDateFin(data.getDateFin());
        existing.setDescription(data.getDescription());
        existing.setVehicule(resolveVehicule(data.getVehicule()));
        return maintenanceRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        maintenanceRepository.delete(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Maintenance> findByVehicule(Long idVehicule) {
        return maintenanceRepository.findByVehiculeIdVehicule(idVehicule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Maintenance> findEnCours() {
        return maintenanceRepository.findByDateFinIsNull();
    }

    // ---------- helpers ----------

    private void verifierDates(Maintenance m) {
        if (m.getDateDebut() == null) {
            throw new BusinessRuleException("La date de début de la maintenance est obligatoire");
        }
        if (m.getDateFin() != null && m.getDateFin().isBefore(m.getDateDebut())) {
            throw new BusinessRuleException("La date de fin ne peut pas précéder la date de début");
        }
    }

    private Vehicule resolveVehicule(Vehicule ref) {
        if (ref == null || ref.getIdVehicule() == null) {
            throw new BusinessRuleException("Le véhicule de la maintenance est obligatoire");
        }
        return vehiculeRepository.findById(ref.getIdVehicule())
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule", ref.getIdVehicule()));
    }
}
