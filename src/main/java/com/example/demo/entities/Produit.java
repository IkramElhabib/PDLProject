package com.example.demo.entities;



import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
public class Produit
{ 
	@Id 
	@NotNull
	private String ref; 
	 private String designation; 
	@DecimalMin("0")  private double prix; 
	@Min(0) private int quantite;
	@Min(0) private int quantiteAlert;
	
	
	private String famille;
	
	/*@ManyToOne
	@JoinColumn(name="CODE_TVA", nullable=true)  
	@NotFound(action = NotFoundAction.IGNORE) 
	private Tva tva;*/

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}

	public int getQuantiteAlert() {
		return quantiteAlert;
	}

	public void setQuantiteAlert(int quantiteAlert) {
		this.quantiteAlert = quantiteAlert;
	}

	public String getFamille() {
		return famille;
	}

	public void setFamille(String famille) {
		this.famille = famille;
	}



	public Produit(String ref, String designation, @DecimalMin("0") double prix, @Min(0) int quantite,
			@Min(0) int quantiteAlert, String famille, Tva tva) {
		super();
		this.ref = ref;
		this.designation = designation;
		this.prix = prix;
		this.quantite = quantite;
		this.quantiteAlert = quantiteAlert;
		this.famille = famille;
		
	}

	public Produit() {
		super();
	}

	public Produit(@NotNull String ref, String designation, @DecimalMin("0") double prix, @Min(0) int quantite,
			@Min(0) int quantiteAlert) {
		super();
		this.ref = ref;
		this.designation = designation;
		this.prix = prix;
		this.quantite = quantite;
		this.quantiteAlert = quantiteAlert;
	}
	
	
	
	

}
