package com.example.demo.IMetier;

import com.example.demo.entities.LigneCommande;

public interface ILcMetier {
	public LigneCommande getLigneCommande( Long id ); 
	public LigneCommande getLigneCommandeByProduit( Long numeroCommande, String ref ); 
	public LigneCommande saveLigneCommande(LigneCommande ligneCommande);
	public boolean deleteLigneCommande(Long id);
}
