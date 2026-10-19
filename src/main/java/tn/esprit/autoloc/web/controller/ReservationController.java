package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.service.IReservationService;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final IReservationService reservationService;

    @GetMapping
    public List<Reservation> retrieveAllReservations() {
        return reservationService.retrieveAllReservations();
    }

    @PostMapping
    public Reservation addReservation(@RequestBody Reservation r) {
        return reservationService.addReservation(r);
    }

    @PutMapping
    public Reservation updateReservation(@RequestBody Reservation r) {
        return reservationService.updateReservation(r);
    }

    @GetMapping("/{id}")
    public Reservation retrieveReservation(@PathVariable Long id) {
        return reservationService.retrieveReservation(id);
    }

    @DeleteMapping("/{id}")
    public void removeReservation(@PathVariable Long id) {
        reservationService.removeReservation(id);
    }
}
