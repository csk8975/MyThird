package com.detection.model.report.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.detection.model.report.entities.CrCheckReportInfo;

/**
 * @fileName CheckReportInfoRepository.java
 * @author csk
 * @createTime 2017年2月28日 上午9:59:35
 * @version 1.0
 * @function
 */

public interface CheckReportInfoRepository extends JpaRepository<CrCheckReportInfo, String> {
    @Query(value="SELECT * FROM cr_check_report_info t WHERE t.report_num like %?1% and t.qa_name like %?2%", nativeQuery = true)
    public List<CrCheckReportInfo> findbyReportNumLikeAndQaNameLike(String reportNum, String QaName);
    
    @Query(value="SELECT t.project_name FROM cr_check_report_info t WHERE t.project_name like %?1%", nativeQuery = true)
    public String findbyOwnerNameLikeProjectName(String ownerName);
}
