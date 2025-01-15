package com.parkingservice.parking.controllers;

import com.parkingservice.parking.controllers.dto.ReservationDTO;
import com.parkingservice.parking.models.Reservation;
import com.parkingservice.parking.services.ReservationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {


    private final ReservationService reservationService;
    private final static Logger logger = LogManager.getLogger(ReservationController.class);

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping

    //

    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationDTO reservationDTO, Long userID) {
        logger.info("Creating new reservation at POST request for /api/reservations. User ID: " + userID);
        try {
            Reservation createdReservation = reservationService.createReservation(reservationDTO, userID);
            return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.warn("Reservation creation failed for user " + userID +
                    ". Provided user ID and reservation user might not match.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getReservationsByUser(Long userID) {
        logger.info("Handling GET request for /api/reservations. User ID: " + userID);
        List<Reservation> reservations = reservationService.getReservationsByUser(userID);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long id, Long userID) {
        logger.info("Cancelling reservation at PATCH request for /api/reservations/" + id + "/cancel. " +
                "User ID: " + userID);
        try {
            Reservation cancelledReservation = reservationService.cancelReservation(id, userID);
            return new ResponseEntity<>(cancelledReservation, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("Reservation cancellation failed. Reservation ID: " + id + ". User ID: " + userID);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Reservation> completeReservation(@PathVariable Long id, Long userID) {
        logger.info("Completing reservation at PATCH request for /api/reservations/" + id + "/complete." +
                " User ID: " + userID);
        try {
            Reservation completedReservation = reservationService.completeReservation(id, userID);
            return new ResponseEntity<>(completedReservation, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("Reservation completion failed. Reservation ID: " + id + ". User ID: " + userID);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/export/excel")
public ResponseEntity<String> exportReservationsToExcel() {
    logger.info("Starting export of reservations to Excel.");
    List<Reservation> reservations = reservationService.getAllReservations();
    String[] columns = {"ID", "User ID", "Spot ID", "Start Time", "End Time", "Status"};

    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Reservations");


    Row headerRow = sheet.createRow(0);
    for (int i = 0; i < columns.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(columns[i]);
        cell.setCellStyle(createHeaderCellStyle(workbook));
    }


    int rowIdx = 1;
    for (Reservation reservation : reservations) {
        Row row = sheet.createRow(rowIdx++);
        row.createCell(0).setCellValue(reservation.getId());
        row.createCell(1).setCellValue(reservation.getUser().getId());
        row.createCell(2).setCellValue(reservation.getParkingSpot().getId());
        row.createCell(3).setCellValue(reservation.getStartTime().toString());
        row.createCell(4).setCellValue(reservation.getEndTime().toString());
        row.createCell(5).setCellValue(reservation.getStatus().toString());
    }


    for (int i = 0; i < columns.length; i++) {
        sheet.autoSizeColumn(i);
    }


    try {
        String userDesktop = System.getProperty("user.home") + "/Desktop";
        Path filePath = Paths.get(userDesktop, "Reservations.xlsx");
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
