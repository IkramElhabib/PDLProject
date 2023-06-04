package com.example.demo.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
 
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateCreation;
	 
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Nullable
	private LocalDate dateFermeture;
	
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

	public LocalDateTime getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(LocalDateTime dateCreation) {
		this.dateCreation = dateCreation;
	}

	public LocalDate getDateFermeture() {
		return dateFermeture;
	}

	public void setDateFermeture(LocalDate dateFermeture) {
		this.dateFermeture = dateFermeture;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Dossier(Long numero, String nom, LocalDateTime dateCreation, LocalDate dateFermeture, User user) {
		super();
		this.numero = numero;
		this.nom = nom;
		this.dateCreation = dateCreation;
		this.dateFermeture = dateFermeture;
		this.user = user;
	}

	public Dossier(Long numero, String nom, LocalDateTime dateCreation, LocalDate dateFermeture) {
		super();
		this.numero = numero;
		this.nom = nom;
		this.dateCreation = dateCreation;
		this.dateFermeture = dateFermeture;
	}

	public Dossier() {
		super();
	}
	public Dossier(Long numero, String nom, LocalDateTime dateCreation) {
		super();
		this.numero = numero;
		this.nom = nom;
		this.dateCreation = dateCreation;
	}
	
	
	
	
	
	
	
}
