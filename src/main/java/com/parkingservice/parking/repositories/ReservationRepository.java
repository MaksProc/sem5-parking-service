package com.parkingservice.parking.repositories;

import com.parkingservice.parking.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Finds reservations made by this user
    List<Reservation> findByUserUsername(String username);
}