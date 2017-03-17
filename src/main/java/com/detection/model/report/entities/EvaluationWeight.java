package com.detection.model.report.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

@Entity
public class EvaluationWeight {
	
	@Id
	@GeneratedValue
	private int Id;
	
	private String LayerName;
	
	private float ClassA;
	
	private float ClassB;
	
	private float ClassC;
	
    private Date createDate;
    
    private Date modifyDate;
    
    private String creatorName;
    
    private String modifierName;
	
	public EvaluationWeight(){
		
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getLayerName() {
		return LayerName;
	}

	public void setLayerName(String layerName) {
		LayerName = layerName;
	}

	public float getClassA() {
		return ClassA;
	}

	public void setClassA(float classA) {
		ClassA = classA;
	}

	public float getClassB() {
		return ClassB;
	}

	public void setClassB(float classB) {
		ClassB = classB;
	}

	public float getClassC() {
		return ClassC;
	}

	public void setClassC(float classC) {
		ClassC = classC;
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
	
	
	
}
