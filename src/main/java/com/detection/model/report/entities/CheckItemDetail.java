/*
 * File Name：CheckItemDetail.java
 *
 * Copyrighe：copyright@2017 www.ggkbigdata.com. All Rights Reserved
 *
 * Create Time: 2017年2月22日 上午10:33:18
 */
package com.detection.model.report.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 *
 * @author lcc (lincc@ggkbigdata.com)
 * @version 1.0, 2017年2月22日 上午10:33:18
 * @function 检测情况统计
 */
@Entity
public class CheckItemDetail {

    @Id
    @GeneratedValue
    private int id;
    // private String reportNum;
    private String itemCode;
    private String importantGrade;
    private String itemName;

    private int checkNum;
    private int unqualifiedNum;

    private String regular;

    public CheckItemDetail() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getImportantGrade() {
        return importantGrade;
    }

    public void setImportantGrade(String importantGrade) {
        this.importantGrade = importantGrade;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }


}
