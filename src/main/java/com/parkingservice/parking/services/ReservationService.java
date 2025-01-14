package com.parkingservice.parking.services;

import com.parkingservice.parking.controllers.dto.ReservationDTO;
import com.parkingservice.parking.models.ParkingSpot;
import com.parkingservice.parking.models.Reservation;
import com.parkingservice.parking.models.ReservationStatus;
import com.parkingservice.parking.models.User;
import com.parkingservice.parking.repositories.ParkingSpotRepository;
import com.parkingservice.parking.repositories.ReservationRepository;
import com.parkingservice.parking.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final UserRepository userRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository,
                              ParkingSpotRepository parkingSpotRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional
    public Reservation createReservation(ReservationDTO reservationDTO, Long userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userID));

        Long spotId = reservationDTO.getParkingSpotID();
        ParkingSpot parkingSpot = parkingSpotRepository.findById(spotId)
                .orElseThrow(() -> new IllegalArgumentException("Parking spot not found with ID " + spotId));


        // Validity check - provided userID must match ID in reservation
        // ...


        Reservation reservation = new Reservation();

        reservation.setParkingSpot(parkingSpot);
        reservation.setUser(user);
        reservation.setStatus(ReservationStatus.RESERVED);
        reservation.setStartTime(LocalDateTime.now());
        reservation.setEndTime(reservationDTO.getEndTime());


        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByUser(Long userID) {
        String username = userRepository.findById(userID)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID " + userID)).getUsername();
        return reservationRepository.findByUserUsername(username);
    }

    @Transactional
    public Reservation cancelReservation(Long reservationId, Long userID) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID: " + reservationId));

        // Checking if this reservation was made by this user
        if (!reservation.getUser().getId().equals(userID)) {
            throw new IllegalArgumentException("You are not authorized to cancel this reservation.");
        }

        if (reservation.getStatus() != ReservationStatus.RESERVED) {
            throw new IllegalArgumentException("Reservation cannot be cancelled anymore.");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation completeReservation(Long reservationId, Long userID) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID: " + reservationId));

        //
        if (!reservation.getUser().getId().equals(userID)) {
            throw new IllegalArgumentException("You are not authorized to complete this reservation.");
        }

        if (reservation.getStatus() != ReservationStatus.COMPLETED) {
            throw new IllegalArgumentException("Reservation is already complete.");
        }

        reservation.setStatus(ReservationStatus.COMPLETED);
        reservation.setEndTime(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }
}