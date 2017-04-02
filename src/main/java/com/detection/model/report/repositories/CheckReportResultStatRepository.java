package com.detection.model.report.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.detection.model.report.entities.CrCheckReportResultStat;

public interface CheckReportResultStatRepository extends JpaRepository<CrCheckReportResultStat, Integer> {

}
