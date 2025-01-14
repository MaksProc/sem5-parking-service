package com.parkingservice.parking.controllers;

import com.parkingservice.parking.controllers.dto.ParkingLotDTO;
import com.parkingservice.parking.models.ParkingLot;
import com.parkingservice.parking.services.ParkingLotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/parking-lots")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }



    @GetMapping("/export/excel")
    @Operation(summary = "Export all parking lots to Excel file",
            description = "Generates an Excel file with data of all parking lots and saves it to the desktop.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "File created successfully"),
                    @ApiResponse(responseCode = "500", description = "Error occurred while creating the file")
            })
    public ResponseEntity<String> exportToExcel() {
        List<ParkingLot> parkingLots = parkingLotService.getAllParkingLots();
        String[] columns = {"ID", "Name", "Address", "Total Spots"};

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Parking Lots");


        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(createHeaderCellStyle(workbook));
        }


        int rowIdx = 1;
        for (ParkingLot parkingLot : parkingLots) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(parkingLot.getId());
            row.createCell(1).setCellValue(parkingLot.getName());
            row.createCell(2).setCellValue(parkingLot.getAddress());
            row.createCell(3).setCellValue(parkingLot.getTotalSpots());
        }


        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }


        try {
            String userDesktop = System.getProperty("user.home") + "/Desktop";
            Path filePath = Paths.get(userDesktop, "ParkingLots.xlsx");
            try (FileOutputStream fileOut = new FileOutputStream(filePath.toFile())) {
                workbook.write(fileOut);
            }
            workbook.close();
            return new ResponseEntity<>("File saved to: " + filePath, HttpStatus.OK);
        } catch (IOException e) {
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
