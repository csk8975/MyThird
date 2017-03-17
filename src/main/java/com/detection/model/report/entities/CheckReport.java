/*
 * File Name：CheckReportInfo.java
 *
 * Copyrighe：copyright@2017 www.ggkbigdata.com. All Rights Reserved
 *
 * Create Time: 2017年2月21日 下午4:16:53
 */
package com.detection.model.report.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author lcc (lincc@ggkbigdata.com)
 * @version 1.0, 2017年2月21日 下午4:16:53
 * @function 检测报告首页
 */

@Entity
public class CheckReport {

    @Id
    private String reportNum;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "reportNum")
    private CheckReportInfo checkReportInfo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "reportNum")
    private List<CheckReportResultStat> checkReportResultStat = new ArrayList<CheckReportResultStat>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "reportNum")
    private List<CheckItemDetail> checkItemDetail = new ArrayList<CheckItemDetail>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "reportNum")
    private List<CheckReportUnqualifiedItemDetail> unqualifiedItemDetail = new ArrayList<CheckReportUnqualifiedItemDetail>();

    private Date createDate;
    private Date modifyDate;
    private String creatorName;
    private String modifierName;
    
    private String verifyToken;
    private String fetchCode;

    public CheckReport() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getReportNum() {
        return reportNum;
    }
    
    public void setReportNum(String reportNum) {
        this.reportNum = reportNum;
    }

    public CheckReportInfo getCheckReportInfo() {
        return checkReportInfo;
    }

    public void setCheckReportInfo(CheckReportInfo checkReportInfo) {
        this.checkReportInfo = checkReportInfo;
    }

    public List<CheckReportResultStat> getCheckReportResultStat() {
        return checkReportResultStat;
    }

    public void setCheckReportResultStat(List<CheckReportResultStat> checkReportResultStat) {
        this.checkReportResultStat = checkReportResultStat;
    }

    public List<CheckItemDetail> getCheckItemDetail() {
        return checkItemDetail;
    }

    public void setCheckItemDetail(List<CheckItemDetail> checkItemDetail) {
        this.checkItemDetail = checkItemDetail;
    }

    public List<CheckReportUnqualifiedItemDetail> getUnqualifiedItemDetail() {
        return unqualifiedItemDetail;
    }

    public void setUnqualifiedItemDetail(List<CheckReportUnqualifiedItemDetail> unqualifiedItemDetail) {
        this.unqualifiedItemDetail = unqualifiedItemDetail;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }

    public String getFetchCode() {
        return fetchCode;
    }

    public void setFetchCode(String fetchCode) {
        this.fetchCode = fetchCode;
    }
    
    
}
