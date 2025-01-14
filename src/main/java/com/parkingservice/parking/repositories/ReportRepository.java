package com.parkingservice.parking.repositories;

import com.parkingservice.parking.models.Report;
import com.parkingservice.parking.models.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByReportType(ReportType reportType);

    List<Report> findByReportDate(LocalDate reportDate);
}