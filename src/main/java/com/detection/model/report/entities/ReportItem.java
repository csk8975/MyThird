/*
 * File Name：ReportItem.java
 *
 * Copyrighe：copyright@2017 www.ggkbigdata.com. All Rights Reserved
 *
 * Create Time: 2017年2月21日 上午9:09:47
 */
package com.detection.model.report.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author lcc (lincc@ggkbigdata.com)
 * @version 1.0, 2017年2月21日 上午9:09:47
 * @function 评分模型用类
 */
@Entity
public class ReportItem {
    @Id
    private String reportNum;
    private String checkLevel;
    private int checkNum;
    private int unqualifiedNum;
    private double ratio;
    
    public ReportItem() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public String getReportNum() {
        return reportNum;
    }
    public void setReportNum(String reportNum) {
        this.reportNum = reportNum;
    }
    public String getCheckLevel() {
        return checkLevel;
    }
    public void setCheckLevel(String checkLevel) {
        this.checkLevel = checkLevel;
    }
    public int getCheckNum() {
        return checkNum;
    }
    public void setCheckNum(int checkNum) {
        this.checkNum = checkNum;
    }
    public int getUnqualifiedNum() {
        return unqualifiedNum;
    }
    public void setUnqualifiedNum(int unqualifiedNum) {
        this.unqualifiedNum = unqualifiedNum;
    }
    public double getRatio() {
        return ratio;
    }
    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}