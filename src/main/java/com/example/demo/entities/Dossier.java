package com.example.demo.entities;

import java.util.Date;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import jakarta.persistence.*;

import lombok.*;

@Entity 
public class Dossier
{
	@Id  @GeneratedValue
	private Long numero;
	
	private String nom;
 
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateCreation;
	 
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Nullable
	private Date dateFermeture;
	
	@ManyToOne
	@JoinColumn(name="USERNAME") 
	@NotFound(action = NotFoundAction.IGNORE)
	private User user;

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateFermeture() {
		return dateFermeture;
	}

	public void setDateFermeture(Date dateFermeture) {
		this.dateFermeture = dateFermeture;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Dossier(Long numero, String nom, Date dateCreation, Date dateFermeture, User user) {
		super();
		this.numero = numero;
		this.nom = nom;
		this.dateCreation = dateCreation;
		this.dateFermeture = dateFermeture;
		this.user = user;
	}
	
	
	
}
