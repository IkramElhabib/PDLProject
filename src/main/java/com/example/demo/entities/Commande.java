package com.example.demo.entities;


import java.util.Collection;
import java.util.Date;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import jakarta.persistence.GeneratedValue;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Commande {
	public long getNumero() {
		return numero;
	}

	public void setNumero(long numero) {
		this.numero = numero;
	}

	public Date getDateCommande() {
		return dateCommande;
	}

	public void setDateCommande(Date dateCommande) {
		this.dateCommande = dateCommande;
	}

	public boolean isValide() {
		return valide;
	}

	public void setValide(boolean valide) {
		this.valide = valide;
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

	public Collection<LigneCommande> getLignesCommande() {
		return lignesCommande;
	}

	public void setLignesCommande(Collection<LigneCommande> lignesCommande) {
		this.lignesCommande = lignesCommande;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Dossier getDossier() {
		return dossier;
	}

	public void setDossier(Dossier dossier) {
		this.dossier = dossier;
	}

	@Id @GeneratedValue
	private long numero;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Nonnull
	private Date dateCommande;
	
	@Nonnull
	private boolean valide;
	
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
	
	@OneToMany(mappedBy="commande",fetch=FetchType.LAZY)
	private Collection<LigneCommande> lignesCommande;
	
	private double total;
	
	@ManyToOne
	@JoinColumn(name="NUM_DOSSIER")
	@NotFound(action = NotFoundAction.IGNORE)
	private Dossier dossier;
}
