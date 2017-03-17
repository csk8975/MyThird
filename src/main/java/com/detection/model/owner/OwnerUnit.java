package com.detection.model.owner;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.detection.model.report.entities.CheckRecord;

@Entity
public class OwnerUnit {

    @Id
    private String dutyTel;

    private String ownerName;

    private String email;

    private String dutyPerson;

    private String token;

    private String authorizedReportNum;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "dutyTel")
    private List<CheckRecord> checkRecords = new ArrayList<CheckRecord>();

    private Date registTime;
    
    private Date loginTime;
    
    private Date tokenTime;

    public OwnerUnit() {
        super();
    }

    public String getDutyTel() {
        return dutyTel;
    }

    public void setDutyTel(String dutyTel) {
        this.dutyTel = dutyTel;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDutyPerson() {
        return dutyPerson;
    }

    public void setDutyPerson(String dutyPerson) {
        this.dutyPerson = dutyPerson;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<CheckRecord> getCheckRecords() {
        return checkRecords;
    }

    public void setCheckRecords(List<CheckRecord> checkRecords) {
        this.checkRecords = checkRecords;
    }

    public Date getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getTokenTime() {
        return tokenTime;
    }

    public void setTokenTime(Date tokenTime) {
        this.tokenTime = tokenTime;
    }

    public String getAuthorizedReportNum() {
        return authorizedReportNum;
    }

    public void setAuthorizedReportNum(String authorizedReportNum) {
        this.authorizedReportNum = authorizedReportNum;
    }

    public void addOneCheckRecord(CheckRecord item) {
        if (item != null) {
            this.checkRecords.add(item);
        }
    }
    
    public boolean hasRecord(String reportNum){
        boolean result = false;
        Iterator<CheckRecord> it = checkRecords.iterator();
        while(it.hasNext()){
            if(it.next().getReportNum().equals(reportNum)){
                result = true;
                break;
            }
        }
        return result;
    }

}
