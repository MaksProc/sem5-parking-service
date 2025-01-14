package com.parkingservice.parking.controllers;

import com.parkingservice.parking.controllers.dto.ParkingSpotDTO;
import com.parkingservice.parking.models.ParkingSpot;
import com.parkingservice.parking.services.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking-spots")
public class ParkingSpotController {


    private final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping

    //

    public ResponseEntity<ParkingSpot> createParkingSpot(@RequestBody ParkingSpotDTO parkingSpotDTO) {
        ParkingSpot createdParkingSpot = parkingSpotService.createParkingSpot(parkingSpotDTO);
        return new ResponseEntity<>(createdParkingSpot, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpot> getParkingSpotById(@PathVariable Long id) {
        try {
            ParkingSpot parkingSpot = parkingSpotService.getParkingSpotById(id);
            return new ResponseEntity<>(parkingSpot, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")

    //

    public ResponseEntity<ParkingSpot> updateParkingSpot(@PathVariable Long id,
                                                         @RequestBody ParkingSpotDTO parkingSpotDTO) {
        try {
            ParkingSpot updatedParkingSpot = parkingSpotService.updateParkingSpot(id, parkingSpotDTO);
            return new ResponseEntity<>(updatedParkingSpot, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingSpot(@PathVariable Long id) {
        try {
            parkingSpotService.deleteParkingSpot(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //
    @GetMapping("/available/{parkingLotId}")
    public ResponseEntity<List<ParkingSpot>> getAvailableSpotsByParkingLotId(@PathVariable Long parkingLotId) { // Изменили тип на Long
        List<ParkingSpot> availableSpots = parkingSpotService.getAvailableSpotsByParkingLotId(parkingLotId);
        return new ResponseEntity<>(availableSpots, HttpStatus.OK);
    }
}