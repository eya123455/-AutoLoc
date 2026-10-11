package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.domain.Reservation;
import tn.esprit.autoloc.autolocapi.domain.StatutReservation;

import java.util.List;

public interface IReservationService {

    Reservation create(Reservation reservation);

    List<Reservation> findAll();

    Reservation findById(Long id);

    Reservation update(Long id, Reservation reservation);

    void delete(Long id);

    List<Reservation> findByClient(Long idClient);

    List<Reservation> findByVehicule(Long idVehicule);

    List<Reservation> findByStatut(StatutReservation statut);
}
