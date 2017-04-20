package com.detection.model.report.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @fileName UnqualifiedCheckPoint.java
 * @author csk
 * @createTime 2017年3月1日 下午3:07:11
 * @version 1.0
 * @function
 */
@Entity
public class CrUnqualifiedCheckPoint {
    @Id
    @GeneratedValue
    private int id;
    @Column(length = 2048)
    private String checkPoint;
    
    public CrUnqualifiedCheckPoint(){
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(String checkPoint) {
        this.checkPoint = checkPoint;
    }
    
    
}
