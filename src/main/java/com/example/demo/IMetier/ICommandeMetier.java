package com.example.demo.IMetier;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.entities.Client;
import com.example.demo.entities.Commande;
import com.example.demo.entities.Dossier;
import com.example.demo.entities.Fournisseur;

public interface ICommandeMetier {
	public List<Commande> getCommandes();
	public Page<Commande> getCommandes(Long numDossier,int page, int size );
	
	public Page<Commande> getCommandesClients(Long numDossier,int page, int size ); 
	public Page<Commande> getCommandesClients(Long numDossier,Date date, int page, int size ); 
	public Page<Commande> getCommandesClients(Long numDossier,Boolean valide, int page, int size ); 
	public Page<Commande> getCommandesClients(Long numDossier,Boolean valide, Date date, int page, int size );
	
	public Page<Commande> getCommandesOfClient(Long numDossier, String code, int page, int size );   
	public Page<Commande> getCommandesOfClient(Long numDossier, String code, Date date, int page, int size ); 
	public Page<Commande> getCommandesOfClient(Long numDossier, String code, Boolean valide, int page, int size ); 
	public Page<Commande> getCommandesOfClient(Long numDossier, String code, Boolean valide, Date date, int page, int size ); 



	public Page<Commande> getCommandesFournisseurs(Long numDossier,int page, int size );
	public Page<Commande> getCommandesFournisseurs(Long numDossier,Date date, int page, int size );
	public Page<Commande> getCommandesFournisseurs(Long numDossier,Boolean valide, int page, int size );
	public Page<Commande> getCommandesFournisseurs(Long numDossier,Boolean valide, Date date, int page, int size );
	
	public Page<Commande> getCommandesOfFournisseur(Long numDossier, String code, int page, int size );
	public Page<Commande> getCommandesOfFournisseur(Long numDossier, String code, Date date, int page,int size);
	public Page<Commande> getCommandesOfFournisseur(Long numDossier, String code, Boolean valide, int page,int size);
	public Page<Commande> getCommandesOfFournisseur(Long numDossier, String code, Boolean valide, Date date, int page,int size);
	

	public int getNombreCommandes(Long numDossier);
	public int getNombreCommandes(Date d1, Date d2);
	
	
	public Commande getCommande( Long num );   
	public Commande saveCommande(Commande commande);
	public Commande saveCommande(Long num, Date date, boolean valide, Client clt, Fournisseur frs,Dossier dos);
	public boolean deleteCommande(Long num);
}
