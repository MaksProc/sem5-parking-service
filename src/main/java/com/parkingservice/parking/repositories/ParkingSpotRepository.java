package com.parkingservice.parking.repositories;

import com.parkingservice.parking.models.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {

    @Query("""
            SELECT ps FROM ParkingSpot ps WHERE ps.parkingLot.id = :parkingLotId AND ps.isAvailable = true
            """)
    List<ParkingSpot> findAvailableSpotsById(@Param("parkingLotId") Long parkingLotId);
}