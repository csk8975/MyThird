package com.detection.model.report.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class CrCheckReportUnqualifiedItemDetail {

    @Id
    @GeneratedValue
    private int id;
    private String testItem;
    private String importantGrade;
    private String requirements;
    //private String itemCode;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "unqualified_id")
    private List<CrUnqualifiedCheckPoint> unqualifiedCheckPoint = new ArrayList<CrUnqualifiedCheckPoint>() ;
    
    
    public CrCheckReportUnqualifiedItemDetail(){
        
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getTestItem() {
        return testItem;
    }


    public void setTestItem(String testItem) {
        this.testItem = testItem;
    }


    public String getImportantGrade() {
        return importantGrade;
    }


    public void setImportantGrade(String importantGrade) {
        this.importantGrade = importantGrade;
    }


    public String getRequirements() {
        return requirements;
    }


    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }


    public List<CrUnqualifiedCheckPoint> getUnqualifiedCheckPoint() {
        return unqualifiedCheckPoint;
    }


    public void setUnqualifiedCheckPoint(List<CrUnqualifiedCheckPoint> unqualifiedCheckPoint) {
        this.unqualifiedCheckPoint = unqualifiedCheckPoint;
    }
    
    public void setUnqualifiedCheckPointByStringList(List<String> unqualifiedCheckPointStringList){
        List<CrUnqualifiedCheckPoint> unqualifiedCheckPoints = new ArrayList<CrUnqualifiedCheckPoint>();
        Iterator<String> it = unqualifiedCheckPointStringList.iterator();
        while(it.hasNext()){
            CrUnqualifiedCheckPoint point = new CrUnqualifiedCheckPoint();
            point.setCheckPoint(it.next());
            unqualifiedCheckPoints.add(point);
        }
        
        this.unqualifiedCheckPoint = unqualifiedCheckPoints;
    }

}
