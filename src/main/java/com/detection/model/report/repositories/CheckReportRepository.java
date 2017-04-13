package com.detection.model.report.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.detection.model.report.entities.CrCheckReport;

public interface CheckReportRepository extends JpaRepository<CrCheckReport, String> {
    
    public List<CrCheckReport> findByFetchCode(String fetchCode);
    
    @Query(value="SELECT t.file_path FROM cr_check_report t WHERE t.report_num = ?1", nativeQuery = true)
    public String findFilePathByReportNum(String reportNum);

    @Query(value="SELECT t.original_name FROM cr_check_report t WHERE t.report_num = ?1", nativeQuery = true)
    public String findOriginalNameByReportNum(String reportNum);
    
    @Query(value="SELECT t.report_num FROM cr_check_report t WHERE t.fetch_code = ?1", nativeQuery = true)
    public String findReportNumByFetchCode(String fetchCode);

}