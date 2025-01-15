package com.parkingservice.parking.services;

import com.parkingservice.parking.controllers.dto.ParkingSpotDTO;
import com.parkingservice.parking.models.ParkingLot;
import com.parkingservice.parking.models.ParkingSpot;
import com.parkingservice.parking.repositories.ParkingLotRepository;
import com.parkingservice.parking.repositories.ParkingSpotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParkingSpotService {


    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingLotRepository parkingLotRepository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository, ParkingLotRepository parkingLotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.parkingLotRepository = parkingLotRepository;
    }

    @Transactional
    public ParkingSpot createParkingSpot(ParkingSpotDTO parkingSpotDTO) {
        ParkingLot parkingLot = parkingLotRepository.findById(parkingSpotDTO.getParkingLotID())
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found with ID " +
                        parkingSpotDTO.getParkingLotID()));

        ParkingSpot parkingSpot = new ParkingSpot();
        parkingSpot.setParkingLot(parkingLot);
        parkingSpot.setSpotNumber(parkingSpotDTO.getSpotNumber());
        parkingSpot.setIsAvailable(parkingSpotDTO.getIsAvailable());

        return parkingSpotRepository.save(parkingSpot);
    }

    public ParkingSpot getParkingSpotById(Long id) {
        return parkingSpotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Parking spot not found with ID: " + id));
    }

    @Transactional
    public ParkingSpot updateParkingSpot(Long id, ParkingSpotDTO parkingSpotDTO) {
        ParkingSpot existingParkingSpot = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Parking spot not found with ID: " + id));

        ParkingLot parkingLot = parkingLotRepository.findById(parkingSpotDTO.getParkingLotID())
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found with ID " +
                        parkingSpotDTO.getParkingLotID()));

        existingParkingSpot.setSpotNumber(parkingSpotDTO.getSpotNumber());
        existingParkingSpot.setIsAvailable(parkingSpotDTO.getIsAvailable());
        existingParkingSpot.setParkingLot(parkingLot);

        return parkingSpotRepository.save(existingParkingSpot);
    }

    @Transactional
    public void deleteParkingSpot(Long id) {
        ParkingSpot parkingSpot = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Parking spot not found with ID: " + id));

        parkingSpotRepository.delete(parkingSpot);
    }

    public List<ParkingSpot> getAvailableSpotsByParkingLotId(Long parkingLotId) {
        return parkingSpotRepository.findAvailableSpotsById(parkingLotId);
    }
    
    @Transactional(readOnly = true)
public List<ParkingSpot> getAllParkingSpots() {
    return parkingSpotRepository.findAll();
}

}
