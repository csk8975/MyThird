package com.detection.model.report.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.detection.model.report.entities.CheckReport;

public interface CheckReportRepository extends JpaRepository<CheckReport, String> {
    
    public List<CheckReport> findByFetchCode(String fetchCode);
}
