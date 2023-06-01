package com.example.demo.entities;


import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LigneFacture
{
	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="NUM_FACTURE")
	private Facture facture; 
	
	@ManyToOne
	@JoinColumn(name="REF_PRODUIT")
	@NotFound(action = NotFoundAction.IGNORE)
	private Produit produit;
	
	private int qte;
	private double prix; 
	
	private double total;
	
	private double ttc;
}
