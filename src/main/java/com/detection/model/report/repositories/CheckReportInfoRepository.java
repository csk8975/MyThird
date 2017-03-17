package com.detection.model.report.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.detection.model.report.entities.CheckReportInfo;

/**
 * @fileName CheckReportInfoRepository.java
 * @author csk
 * @createTime 2017年2月28日 上午9:59:35
 * @version 1.0
 * @function
 */

public interface CheckReportInfoRepository extends JpaRepository<CheckReportInfo, String> {
    
}
