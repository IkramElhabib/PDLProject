package com.example.demo.Controller;

import java.util.Date;
import java.util.List;

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
import com.example.demo.entities.Client;
import com.example.demo.entities.Commande;
import com.example.demo.entities.Dossier;
import com.example.demo.entities.Facture;

@Controller
public class FacturesController {
	
	FactureRepository factureRepository;
	CommandeRepository commandeRepository;
	DossierRepository dossierRepository;
	ClientRepository clientRepository;
	
	public FacturesController(FactureRepository factureRepository, CommandeRepository commandeRepository, DossierRepository dossierRepository, ClientRepository clientRepository) {
        this.factureRepository = factureRepository;
        this.commandeRepository = commandeRepository;
        this.dossierRepository = dossierRepository;
        this.clientRepository = clientRepository;
    }
	
	 @GetMapping("/ajouter-facture/{numeroCommande}")
	    public String afficherFormulaireFacture(@PathVariable("numeroCommande") long numeroCommande, Model model) {
	        Commande commande = commandeRepository.findByNumero(numeroCommande);
	        List<Dossier> dossiers = dossierRepository.findByDateFermetureIsNull();

	        model.addAttribute("numeroCommande", commande.getNumero());
	        model.addAttribute("client", commande.getClient());
	        model.addAttribute("dossiers", dossiers);

	        Facture facture = new Facture();
	        
	        // Création d'un nouvel objet Facture avec les valeurs initiales
	        facture.setCommande(commande);
	        facture.setClient(commande.getClient());
	        
	        model.addAttribute("facture", facture);
	        return "facture";
	    }
	 
	 @PostMapping("/sauvegarder-facture")
	    public String sauvegarderFacture(@ModelAttribute("facture") Facture facture, Model model) {
		 
		 	Client client = facture.getClient();
		 	 if (client.getCode() == null) {
		         // Create a new client instance
		         client = new Client();
		         client.setNom(facture.getClient().getNom());
		         // Set other client properties as needed
		     } else {
		         // Retrieve the existing client from the database
		         client = clientRepository.findById(client.getCode()).orElse(null);
		     }
		 	 // Set the client in the facture
		     facture.setClient(client);

	        
	        // Mettre à jour l'état de validité de la commande
	        Commande commande = facture.getCommande();
	        commande.setValide(true);
	        
	     // Récupérer le dossier sélectionné par son ID
	        Long dossierId = facture.getDossier().getNumero();
	        Dossier dossier = dossierRepository.findById(dossierId).orElse(null);

	        // Associer le dossier à la facture
	        facture.setDossier(dossier);
	        
	        commandeRepository.save(commande);
	        factureRepository.save(facture);
		 
	        return "redirect:/factureslist";
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