package com.example.demo.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
public class Tva
{
	@Id @GeneratedValue 
	private Long code; 
	private String designation;
	private float taux;
	
	public Tva(Long code, String designation, float taux) {
		super();
		this.code = code;
		this.designation = designation;
		this.taux = taux;
	}
	public Tva() {
		super();
	}
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public float getTaux() {
		return taux;
	}
	public void setTaux(float taux) {
		this.taux = taux;
	}
	
	

}
