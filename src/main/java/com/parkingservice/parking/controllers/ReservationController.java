package com.parkingservice.parking.controllers;

import com.parkingservice.parking.controllers.dto.ReservationDTO;
import com.parkingservice.parking.models.Reservation;
import com.parkingservice.parking.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {


    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping

    //

    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationDTO reservationDTO, Long userID) {
        try {
            Reservation createdReservation = reservationService.createReservation(reservationDTO, userID);
            return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Exceptions may be thrown by the service
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getReservationsByUser(Long userID) {
        List<Reservation> reservations = reservationService.getReservationsByUser(userID);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long id, Long userID) {
        try {
            Reservation cancelledReservation = reservationService.cancelReservation(id, userID);
            return new ResponseEntity<>(cancelledReservation, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Reservation> completeReservation(@PathVariable Long id, Long userID) {
        try {
            Reservation completedReservation = reservationService.completeReservation(id, userID);
            return new ResponseEntity<>(completedReservation, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}