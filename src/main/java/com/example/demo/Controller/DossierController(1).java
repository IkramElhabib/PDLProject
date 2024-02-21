package com.example.demo.Controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import com.example.demo.IMetier.ICommandeMetier;
import com.example.demo.IMetier.IDossierMetier;
import com.example.demo.IMetier.IFactureMetier;
import com.example.demo.IMetier.IUserMetier;
import com.example.demo.dao.CommandeRepository;
import com.example.demo.dao.DossierRepository;
import com.example.demo.dao.FactureRepository;
import com.example.demo.entities.Commande;
import com.example.demo.entities.Dossier;
import com.example.demo.entities.Facture;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class DossierController {
	@Autowired private IDossierMetier metierDossier;  
	@Autowired private ICommandeMetier metierCmd;  
	@Autowired private IFactureMetier metierFct; 
	@Autowired private IUserMetier metierUser;    
	@Autowired private HttpSession session;
	
	DossierRepository dossierRepository;
	CommandeRepository commandeRepository;
	FactureRepository factureRepository;
	
	public DossierController(DossierRepository dossierRepository,CommandeRepository commandeRepository,	FactureRepository factureRepository) {
		this.dossierRepository=dossierRepository;
		this.commandeRepository=commandeRepository;
		this.factureRepository = factureRepository;
	}
	
         
 		
	@GetMapping("/dossiers")
	public String affichagedossier(Model model) {
		 List<Dossier> dossiers = dossierRepository.findAll();
		 
	        model.addAttribute("dossiers", dossiers);
	        return "dossiers";
	}
	
	
	
	
	
	@PostMapping("/dossiers")
	public String creerDossier(@ModelAttribute("dossier") Dossier dossier) {
	    dossier.setDateCreation(LocalDateTime.now());
	    dossierRepository.save(dossier);
	    
	    // Redirect to the appropriate page
	    return "redirect:/dossiers";
	}

    
    @PostMapping("/dossiers/{numero}/fermer")
    public String fermerDossier(@PathVariable("numero") Long numero) {
        Dossier dossier = dossierRepository.findById(numero).orElse(null);
        if (dossier != null) {
            dossier.setDateFermeture(LocalDate.now());
            dossierRepository.save(dossier);
        }
        
        // Redirection vers une autre page ou une réponse appropriée
        return "redirect:/dossiers";
    }
    
    @GetMapping("/dossier/open")
    public String ajouterDossier(Model model) {
	 model.addAttribute("dossier", new Dossier());
        return "ouvrirdossier";
    }
 
 @PostMapping("/dossieradd")
 public String ajouterDossier(@ModelAttribute @Valid Dossier dossier, BindingResult bindingResult, Model model) {
     if (bindingResult.hasErrors()) {
         // If there are validation errors, return to the form with the error messages
         return "listProduits";
     }
     Dossier existingDossier = dossierRepository.findByNumero(dossier.getNumero());
     if (existingDossier != null) {
         // Add a field error for the "code" field
         bindingResult.rejectValue("numero", "error.dossier", "Ce nom de dossier existe déjà. Veuillez la changer.");
         return "ouvrirdossier";
     }

     dossierRepository.save(dossier);

     // Add success message to the model
     model.addAttribute("successMessage", "Dossier crée avec succès.");

     return "redirect:/fournisseur/add";
 }
 @GetMapping("/detailss/{numero}")
 public String showDossierDetails(@PathVariable("numero") Long numero, Model model) {
     Dossier dossier = dossierRepository.findById(numero).orElse(null);
     if (dossier == null) {
         return "redirect:/dossiers";
     }

     List<Commande> commandes = commandeRepository.findByDossier(dossier);
     List<Facture> factures = factureRepository.findByDossier(dossier);
    
     model.addAttribute("dossier", dossier);
     model.addAttribute("commandes", commandes);
     model.addAttribute("factures", factures);

     List<Dossier> dossiers = dossierRepository.findAll(); // Retrieve all dossiers
     model.addAttribute("dossiers", dossiers);

     return "dossierdetails";
 }

 
	
}
