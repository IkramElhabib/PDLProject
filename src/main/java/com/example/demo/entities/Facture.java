package com.example.demo.entities;

import java.util.Collection;
import java.util.Date;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import jakarta.persistence.*;

import lombok.*;


@Entity 
public class Facture
{
	@Id @GeneratedValue
	private Long numero;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateFacture;
	 
	private double total;
	private double ttc;
	 
	@ManyToOne
	@JoinColumn(name="CODE_CLIENT")
	@Nullable
	@NotFound(action = NotFoundAction.IGNORE)
	private Client client;
	
	@ManyToOne 
	@JoinColumn(name="CODE_FOURNISSEUR")
	@Nullable
	@NotFound(action = NotFoundAction.IGNORE)
	private Fournisseur fournisseur;
	
	@ManyToOne
	@JoinColumn(name="NUM_COMMANDE")
	private Commande commande;
 
	@OneToMany(mappedBy="facture",fetch=FetchType.LAZY)
	private Collection<LigneFacture> lignesFacture;
	
 

	@ManyToOne
	@JoinColumn(name="NUM_DOSSIER")
	@NotFound(action = NotFoundAction.IGNORE)
	private Dossier dossier;



	public Long getNumero() {
		return numero;
	}



	public void setNumero(Long numero) {
		this.numero = numero;
	}



	public Date getDateFacture() {
		return dateFacture;
	}



	public void setDateFacture(Date dateFacture) {
		this.dateFacture = dateFacture;
	}



	public double getTotal() {
		return total;
	}



	public void setTotal(double total) {
		this.total = total;
	}



	public double getTtc() {
		return ttc;
	}



	public void setTtc(double ttc) {
		this.ttc = ttc;
	}



	public Client getClient() {
		return client;
	}



	public void setClient(Client client) {
		this.client = client;
	}



	public Fournisseur getFournisseur() {
		return fournisseur;
	}



	public void setFournisseur(Fournisseur fournisseur) {
		this.fournisseur = fournisseur;
	}



	public Commande getCommande() {
		return commande;
	}



	public void setCommande(Commande commande) {
		this.commande = commande;
	}



	public Collection<LigneFacture> getLignesFacture() {
		return lignesFacture;
	}



	public void setLignesFacture(Collection<LigneFacture> lignesFacture) {
		this.lignesFacture = lignesFacture;
	}



	public Dossier getDossier() {
		return dossier;
	}



	public void setDossier(Dossier dossier) {
		this.dossier = dossier;
	}



	public Facture(Long numero, Date dateFacture, double total, double ttc, Client client, Fournisseur fournisseur,
			Commande commande, Collection<LigneFacture> lignesFacture, Dossier dossier) {
		super();
		this.numero = numero;
		this.dateFacture = dateFacture;
		this.total = total;
		this.ttc = ttc;
		this.client = client;
		this.fournisseur = fournisseur;
		this.commande = commande;
		this.lignesFacture = lignesFacture;
		this.dossier = dossier;
	}



	public Facture() {
		super();
	}
	
	
	
	
	
}
