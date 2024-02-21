package com.example.demo.IMetier;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.entities.Produit;

public interface IProduitMetier {
	public List<Produit> getProduits();
	public Page<Produit> getProduitsByMotCle( String mc, int page, int size );
	public Produit getProduit( String ref );   
	public Produit saveProduit(Produit produit);
	public boolean deleteProduit(String ref);
}
