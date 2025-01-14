package com.parkingservice.parking.controllers;

import com.parkingservice.parking.controllers.dto.ParkingLotDTO;
import com.parkingservice.parking.controllers.dto.UserDTO;
import com.parkingservice.parking.models.ParkingLot;
import com.parkingservice.parking.services.ParkingLotService;
import com.parkingservice.parking.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking-lots")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;
    private static final Logger logger = LogManager.getLogger(ParkingLotController.class);

    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @PostMapping
    @Operation(summary = "Create a new parking lot",
            description = "Requires name, address and total spots count of a new parking lot.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Creation successful")
            })
    public ResponseEntity<ParkingLot> createParkingLot(@RequestBody ParkingLotDTO parkingLotDTO) {
        logger.info("Handling POST request for /api/parking-lots.");
        ParkingLot createdParkingLot = parkingLotService.createParkingLot(
                parkingLotDTO.getName(),
                parkingLotDTO.getAddress(),
                parkingLotDTO.getTotalSpots()
        );
        return new ResponseEntity<>(createdParkingLot, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<ParkingLot>> getAllParkingLots() {
        logger.info("Handling GET request for /api/parking-lots.");
        List<ParkingLot> parkingLots = parkingLotService.getAllParkingLots();
        return new ResponseEntity<>(parkingLots, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get parking lot by ID",
            description = "Returns a parking lot based on its ID. \n" +
            "Throws IllegalArgumentException if not found.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched the parking lot"),
                    @ApiResponse(responseCode = "404", description = "Parking lot not found")
            })
    public ResponseEntity<ParkingLot> getParkingLotById(@PathVariable Long id) {
        logger.info("Handling GET request for /api/parking-lots/" + id);
        try {
            ParkingLot parkingLot = parkingLotService.getParkingLotById(id);
            return new ResponseEntity<>(parkingLot, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("Data not found for GET request for /api/parking-lots/" + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update data of existing parking lot",
            description = "Requires ID of existing parking lot, and updates its name, address and total spots count " +
                    "with ones provided.",
            responses = {
                @ApiResponse(responseCode = "400", description = "Parking lot not found"),
                @ApiResponse(responseCode = "200", description = "Update successful")
            })
    public ResponseEntity<ParkingLot> updateParkingLot(@PathVariable Long id,
                                                       @RequestBody ParkingLotDTO parkingLotDTO
    ) {
        logger.info("Handling PUT request for /api/parking-lots/" + id);
        try {
            ParkingLot updatedParkingLot = parkingLotService.updateParkingLot(
                    id,
                    parkingLotDTO.getName(),
                    parkingLotDTO.getAddress(),
                    parkingLotDTO.getTotalSpots()
            );
            return new ResponseEntity<>(updatedParkingLot, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("Bad PUT request for /api/parking-lots/" + id + ". Cause: " + e.getCause());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingLot(@PathVariable Long id) {
        logger.info("Handling DEL request for /api/parking-lots/" + id);
        try {
            parkingLotService.deleteParkingLot(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}