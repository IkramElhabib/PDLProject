package com.example.demo.entities;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.*;


@Entity
public class LigneCommande
{
	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="NUM_COMMANDE")
	private Commande commande;
	
	@ManyToOne
	@JoinColumn(name="REF_PRODUIT")
	@NotFound(action = NotFoundAction.IGNORE)
	private Produit produit;
	
	private int qte;
	
	private double total;
	
	private double ttc;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Commande getCommande() {
		return commande;
	}

	public void setCommande(Commande commande) {
		this.commande = commande;
	}

	public Produit getProduit() {
		return produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}

	public int getQte() {
		return qte;
	}

	public void setQte(int qte) {
		this.qte = qte;
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

	public LigneCommande(Long id, Commande commande, Produit produit, int qte, double total, double ttc) {
		super();
		this.id = id;
		this.commande = commande;
		this.produit = produit;
		this.qte = qte;
		this.total = total;
		this.ttc = ttc;
	}

	public LigneCommande(Commande commande, Produit produit, int qte) {
		super();
		this.commande = commande;
		this.produit = produit;
		this.qte = qte;
	}

	public LigneCommande() {
		super();
	}
	
	
	
}
