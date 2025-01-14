package com.parkingservice.parking.controllers;

import com.parkingservice.parking.controllers.dto.ParkingSpotDTO;
import com.parkingservice.parking.models.ParkingSpot;
import com.parkingservice.parking.services.ParkingSpotService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking-spots")
public class ParkingSpotController {


    private final ParkingSpotService parkingSpotService;
    private static final Logger logger = LogManager.getLogger(ParkingSpotController.class);

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping

    //

    public ResponseEntity<ParkingSpot> createParkingSpot(@RequestBody ParkingSpotDTO parkingSpotDTO) {
        logger.info("Creating parking spot at POST request for /api/parking-spots. " +
                "Spot number: " + parkingSpotDTO.getSpotNumber());
        ParkingSpot createdParkingSpot = parkingSpotService.createParkingSpot(parkingSpotDTO);
        return new ResponseEntity<>(createdParkingSpot, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpot> getParkingSpotById(@PathVariable Long id) {
        logger.info("Handling GET for /api/parking-spots/" + id);
        try {
            ParkingSpot parkingSpot = parkingSpotService.getParkingSpotById(id);
            return new ResponseEntity<>(parkingSpot, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("Parking spot not found with ID " + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")

    //

    public ResponseEntity<ParkingSpot> updateParkingSpot(@PathVariable Long id,
                                                         @RequestBody ParkingSpotDTO parkingSpotDTO) {
        logger.info("Updating parking spot at PUT request for /api/parking-spots/" + id);
        try {
            ParkingSpot updatedParkingSpot = parkingSpotService.updateParkingSpot(id, parkingSpotDTO);
            return new ResponseEntity<>(updatedParkingSpot, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("Bad request for spot update. ID: " + id + ". Cause: " + e.getCause());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingSpot(@PathVariable Long id) {
        logger.info("Handling DEL request for /api/parking-spots/" + id);
        try {
            parkingSpotService.deleteParkingSpot(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            logger.warn("Deletion failed. Spot not found with ID " + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //
    @GetMapping("/available/{parkingLotId}")
    public ResponseEntity<List<ParkingSpot>> getAvailableSpotsByParkingLotId(@PathVariable Long parkingLotId) {
        List<ParkingSpot> availableSpots = parkingSpotService.getAvailableSpotsByParkingLotId(parkingLotId);
        return new ResponseEntity<>(availableSpots, HttpStatus.OK);
    }
}