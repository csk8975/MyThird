package com.detection.model.report.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.detection.model.report.entities.ReportItem;

public interface ReportItemRepository extends JpaRepository<ReportItem, String> {
}
