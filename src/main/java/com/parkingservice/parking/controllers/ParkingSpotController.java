package com.parkingservice.parking.controllers;

import com.parkingservice.parking.controllers.dto.ParkingSpotDTO;
import com.parkingservice.parking.models.ParkingSpot;
import com.parkingservice.parking.services.ParkingSpotService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @GetMapping("/available/{parkingLotId}")
    public ResponseEntity<List<ParkingSpot>> getAvailableSpotsByParkingLotId(@PathVariable Long parkingLotId) {
        List<ParkingSpot> availableSpots = parkingSpotService.getAvailableSpotsByParkingLotId(parkingLotId);
        return new ResponseEntity<>(availableSpots, HttpStatus.OK);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<String> exportToExcel() {
        logger.info("Starting export of parking spots to Excel.");
        List<ParkingSpot> parkingSpots = parkingSpotService.getAllParkingSpots();
        String[] columns = {"ID", "Spot Number", "Occupied", "Parking Lot ID"};

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Parking Spots");


        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(createHeaderCellStyle(workbook));
        }


        int rowIdx = 1;
        for (ParkingSpot spot : parkingSpots) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(spot.getId());
            row.createCell(1).setCellValue(spot.getSpotNumber());
            row.createCell(2).setCellValue(spot.isOccupied() ? "Yes" : "No");
            row.createCell(3).setCellValue(spot.getParkingLot().getId());
        }


        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try {
            String userDesktop = System.getProperty("user.home") + "/Desktop";
            Path filePath = Paths.get(userDesktop, "ParkingSpots.xlsx");
            try (FileOutputStream fileOut = new FileOutputStream(filePath.toFile())) {
                workbook.write(fileOut);
            }
            workbook.close();
            logger.info("Excel file saved to: " + filePath);
            return new ResponseEntity<>("File saved to: " + filePath, HttpStatus.OK);
        } catch (IOException e) {
            logger.error("Error saving Excel file: " + e.getMessage());
            return new ResponseEntity<>("Error saving file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
