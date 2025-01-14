package com.parkingservice.parking.services;

import com.parkingservice.parking.models.Report;
import com.parkingservice.parking.models.ReportType;
import com.parkingservice.parking.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {


    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReportById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found with ID: " + id));
    }

    public List<Report> getReportsByType(String reportType) {
        return reportRepository.findByReportType(ReportType.valueOf(reportType));
    }

    public List<Report> getReportsByDate(String reportDate) {
        LocalDate date = LocalDate.parse(reportDate);
        return reportRepository.findByReportDate(date);
    }
}