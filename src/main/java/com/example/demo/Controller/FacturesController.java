package com.example.demo.Controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.CommandeRepository;
import com.example.demo.dao.FactureRepository;
import com.example.demo.entities.Commande;
import com.example.demo.entities.Facture;

@Controller
public class FacturesController {
	
	FactureRepository factureRepository;
	CommandeRepository commandeRepository;
	
	public FacturesController(FactureRepository factureRepository, CommandeRepository commandeRepository) {
        this.factureRepository = factureRepository;
        this.commandeRepository = commandeRepository;
    }
	
	 @GetMapping("/ajouter-facture/{numeroCommande}")
	    public String afficherFormulaireFacture(@PathVariable("numeroCommande") long numeroCommande, Model model) {
	        Commande commande = commandeRepository.findByNumero(numeroCommande);
	        model.addAttribute("numeroCommande", commande.getNumero());
	        model.addAttribute("client", commande.getClient());
	        
	        // Création d'un nouvel objet Facture avec les valeurs initiales
	        Facture facture = new Facture();
	        facture.setCommande(commande);
	        facture.setClient(commande.getClient());
	        
	        model.addAttribute("facture", facture);
	        return "facture";
	    }
	 
	 @PostMapping("/sauvegarder-facture")
	    public String sauvegarderFacture(@ModelAttribute("facture") Facture facture, Model model) {
		 factureRepository.save(facture);
	        
	        // Mettre à jour l'état de validité de la commande
	        Commande commande = facture.getCommande();
	        commande.setValide(true);
	        commandeRepository.save(commande);
		 
	        return "redirect:/commandeslist";
	    }


}
