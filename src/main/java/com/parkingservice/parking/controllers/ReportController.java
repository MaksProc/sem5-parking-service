package com.parkingservice.parking.controllers;

import com.parkingservice.parking.models.Report;
import com.parkingservice.parking.models.ReportType;
import com.parkingservice.parking.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {


    private final ReportService reportService;

    public ReportController (ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        Report createdReport = reportService.createReport(report);
        return new ResponseEntity<>(createdReport, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        try {
            Report report = reportService.getReportById(id);
            return new ResponseEntity<>(report, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/type/{reportType}")
    public ResponseEntity<List<Report>> getReportsByType(@PathVariable String reportType) {
        try {
            ReportType type = ReportType.valueOf(reportType);
            List<Report> reports = reportService.getReportsByType(String.valueOf(type));
            return new ResponseEntity<>(reports, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Incorrect report type
        }
    }

    @GetMapping("/date/{reportDate}")
    public ResponseEntity<List<Report>> getReportsByDate(@PathVariable String reportDate) {
        try {
            LocalDate date = LocalDate.parse(reportDate);
            List<Report> reports = reportService.getReportsByDate(String.valueOf(date));
            return new ResponseEntity<>(reports, HttpStatus.OK);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Incorrect date format
        }
    }

}