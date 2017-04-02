package com.detection.model.report.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.detection.model.report.entities.CrReportItem;

public interface ReportItemRepository extends JpaRepository<CrReportItem, String> {
}
