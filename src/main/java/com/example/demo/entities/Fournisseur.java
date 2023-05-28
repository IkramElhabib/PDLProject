package com.example.demo.entities;


import org.springframework.lang.Nullable;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.*;


@Entity
public class Fournisseur
{ 
	@Id @Nonnull 
	private String code;
	
	
	private String nom;
	
	@Email @NotBlank
	private String email;
	
	
	private String raisonSociale;
	
	
	private String adresse;
	
	@Nullable
	private String compteBancaire;
	
	@Min(1000) 
	@Nonnull
	private Double  capital;
	

	@Pattern(regexp="^(0[0-9]{9,})$",message="Numéro de télephone doit etre composé au minimum de 10 nombres !")
	private String tel;
	

	@Pattern(regexp="^(0[0-9]{9,})$",message="Fax doit etre composé au minimum  de 10 nombres !")
	private String fax;


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getRaisonSociale() {
		return raisonSociale;
	}


	public void setRaisonSociale(String raisonSociale) {
		this.raisonSociale = raisonSociale;
	}


	public String getAdresse() {
		return adresse;
	}


	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}


	public String getCompteBancaire() {
		return compteBancaire;
	}


	public void setCompteBancaire(String compteBancaire) {
		this.compteBancaire = compteBancaire;
	}


	public Double getCapital() {
		return capital;
	}


	public void setCapital(Double capital) {
		this.capital = capital;
	}


	public String getTel() {
		return tel;
	}


	public void setTel(String tel) {
		this.tel = tel;
	}


	public String getFax() {
		return fax;
	}


	public void setFax(String fax) {
		this.fax = fax;
	}


	public Fournisseur(String code, String nom, @Email @NotBlank String email, String raisonSociale, String adresse,
			String compteBancaire, @Min(1000) Double capital,
			@Pattern(regexp = "^(0[0-9]{9,})$", message = "Numéro de télephone doit etre composé au minimum de 10 nombres !") String tel,
			@Pattern(regexp = "^(0[0-9]{9,})$", message = "Fax doit etre composé au minimum  de 10 nombres !") String fax) {
		super();
		this.code = code;
		this.nom = nom;
		this.email = email;
		this.raisonSociale = raisonSociale;
		this.adresse = adresse;
		this.compteBancaire = compteBancaire;
		this.capital = capital;
		this.tel = tel;
		this.fax = fax;
	}


	public Fournisseur() {
		super();
	} 
	
	
	
}
