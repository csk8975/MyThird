package com.detection.model.report.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @fileName CheckRecord.java
 * @author csk
 * @createTime 2017年3月2日 下午6:30:18
 * @version 1.0
 * @function
 */
@Entity
public class CrCheckRecord {
    @Id
    @GeneratedValue
    private int id;
    
    private String reportNum;
    
    private Date recordDate;
    
    public CrCheckRecord(){
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReportNum() {
        return reportNum;
    }

    public void setReportNum(String reportNum) {
        this.reportNum = reportNum;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }
    
    
}
