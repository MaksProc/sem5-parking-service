package com.parkingservice.parking.services;

import com.parkingservice.parking.models.ParkingLot;
import com.parkingservice.parking.repositories.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;

    public ParkingLotService(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    @Transactional
    public ParkingLot createParkingLot(String name, String address, Integer totalSpots) {
        ParkingLot createdParkingLot = new ParkingLot();
        createdParkingLot.setName(name);
        createdParkingLot.setAddress(address);
        createdParkingLot.setTotalSpots(totalSpots);
        return parkingLotRepository.save(createdParkingLot);
    }

    public List<ParkingLot> getAllParkingLots() {
        return parkingLotRepository.findAll();
    }

    public ParkingLot getParkingLotById(Long id) {
        return parkingLotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found with ID: " + id));
    }

    @Transactional
    public ParkingLot updateParkingLot(Long id, String name, String address, Integer totalSpots) {
        ParkingLot existingParkingLot = parkingLotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found with ID: " + id));

        existingParkingLot.setName(name);
        existingParkingLot.setAddress(address);
        existingParkingLot.setTotalSpots(totalSpots);


        return parkingLotRepository.save(existingParkingLot);
    }

    @Transactional
    public void deleteParkingLot(Long id) {
        ParkingLot parkingLot = parkingLotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found with ID: " + id));

        parkingLotRepository.delete(parkingLot);
    }
}