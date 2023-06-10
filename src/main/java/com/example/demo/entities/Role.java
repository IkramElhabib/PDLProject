package com.example.demo.entities;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Role
{
	@Id @NotNull 
	@GeneratedValue(strategy = GenerationType.AUTO) 
	private Long id;
	
	
	 private String role;
	
	@Size(min=3,max=100) 
	 private String designation;

	public String getRole() { 
		return role; 
		} 
	public void setRole(String role) { 
		this.role = role; 
		}
	
	
	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Role() {} 

	public Role(String r) {
		this.role=r;
		}
	public Role(Long id,String r) {
		this.role=r;
		this.id=id;
		}
	public Role(Long id,String r,String designation) {
		this.role=r;
		this.id=id;
		this.designation=designation;
		}
	public Role(String r,String designation) {
		this.role=r;
		this.designation=designation;
		}
	public Role(String designation,Long id) {
		this.id=id ;
		this.designation=designation;
		}

	
}

