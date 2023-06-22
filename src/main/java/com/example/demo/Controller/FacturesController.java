package com.example.demo.Controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.ClientRepository;
import com.example.demo.dao.CommandeRepository;
import com.example.demo.dao.DossierRepository;
import com.example.demo.dao.FactureRepository;
import com.example.demo.dao.FournisseurRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Commande;
import com.example.demo.entities.Dossier;
import com.example.demo.entities.Facture;
import com.example.demo.entities.Fournisseur;
import com.example.demo.entities.LigneCommande;

@Controller
public class FacturesController {
	
	FactureRepository factureRepository;
	CommandeRepository commandeRepository;
	DossierRepository dossierRepository;
	ClientRepository clientRepository;
	@Autowired
	FournisseurRepository fournisseurRepository;
	
	public FacturesController(FactureRepository factureRepository, CommandeRepository commandeRepository, DossierRepository dossierRepository, ClientRepository clientRepository) {
        this.factureRepository = factureRepository;
        this.commandeRepository = commandeRepository;
        this.dossierRepository = dossierRepository;
        this.clientRepository = clientRepository;
    }
	
	 @GetMapping("/ajouter-facture/{numeroCommande}")
	    public String afficherFormulaireFacture(@PathVariable("numeroCommande") long numeroCommande, Model model) {
		  Facture facture = new Facture();   
		 Commande commande = commandeRepository.findByNumero(numeroCommande);
	        List<Dossier> dossiers = dossierRepository.findByDateFermetureIsNull();

	        model.addAttribute("numeroCommande", commande.getNumero());
	       
	        if (commande.getClient() != null) {
	            // Si la commande a un client, définissez-le sur l'objet "commande"
	            commande.getClient().setCode(commande.getClient().getCode());
	            model.addAttribute("client", commande.getClient());
	        }

	        if (commande.getFournisseur() != null) {
	            // Si la commande a un fournisseur, définissez-le sur l'objet "commande"
	            commande.getFournisseur().setCode(commande.getFournisseur().getCode());
	            model.addAttribute("fournisseur", commande.getFournisseur());
	        }

	    	
	        model.addAttribute("dossiers", dossiers);
	        
	        double totalLigneCommande = 0.0;
	        Collection<LigneCommande> lignesCommande = commande.getLignesCommande();

	     // Assuming you have a collection of LigneCommande objects called lignesCommande
	     for (LigneCommande ligneCommande : lignesCommande) {
	         totalLigneCommande += ligneCommande.getTotal();
	     }
  
	        facture.setCommande(commande);

	       
	        facture.setTotal(totalLigneCommande);

	        model.addAttribute("facture", facture);
	        return "facture";
	    }
	 
	 @PostMapping("/sauvegarder-facture")
	    public String sauvegarderFacture(@ModelAttribute("facture") Facture facture, Model model) {
		 	
		 	Commande commande = facture.getCommande();

		 	if (commande.getClient() == null && commande.getFournisseur() != null) {
		 		  Fournisseur fournisseur = commande.getFournisseur();
		 	        facture.setFournisseur(fournisseur);
		        facture.setFournisseur(fournisseur);
		    } else if (commande.getFournisseur() == null && commande.getClient() != null) {
		    	 Client client = commande.getClient();
		         facture.setClient(client);
		       
		    }
	        
	        // Mettre à jour l'état de validité de la commande
	        commande.setValide(true);
	        
	     // Récupérer le dossier sélectionné par son ID
	        Long dossierId = facture.getDossier().getNumero();
	        Dossier dossier = dossierRepository.findById(dossierId).orElse(null);

	        // Associer le dossier à la facture
	        facture.setDossier(dossier);
	        commandeRepository.save(commande);
	        factureRepository.save(facture);
		 
	        
	        return "redirect:/commandeslist";
	    }
		 @GetMapping("/factureslist")
		    public String afficherFactures(Model model) {
		        List<Facture> factures = factureRepository.findAll();
		        model.addAttribute("factures", factures);
		        
		        return "listFactures";
		    }
		 
		 @GetMapping("/modifier-facture/{numero}")
		 public String afficherFormulaireModificationFacture(@PathVariable("numero") Long numero, Model model) {
		     Facture facture = factureRepository.findByNumero(numero);	    
		     model.addAttribute("facture", facture);
		     List<Dossier> dossiers = dossierRepository.findAll();
		     model.addAttribute("dossiers", dossiers);
		     return "updatefacture"; 
		 }

		 @PostMapping("/modifier-facture")
		 public String modifierFacture(@ModelAttribute("facture") Facture facture, Model model) {
			Client client = clientRepository.findByCode(facture.getClient().getCode());
			Dossier dossier = dossierRepository.findByNumero(facture.getDossier().getNumero());

		     if (client != null) {
		         facture.setClient(client); 
		         facture.setDossier(dossier);
		         
		         factureRepository.save(facture); 
		     } 
		     return "redirect:/factureslist"; 
		 }


}