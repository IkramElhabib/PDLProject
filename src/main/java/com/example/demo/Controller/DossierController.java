package com.example.demo.Controller;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
import com.example.demo.entities.User;

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
	
	@RequestMapping(value= {"/dossiers"})
	public String index( 
			Model model,
			@RequestParam(name="page",defaultValue="0")	Integer p, 
			@RequestParam(name="size",defaultValue="8")	Integer s, 
			@RequestParam(name="type",defaultValue="")	String  type,
			@RequestParam(name="dateo",defaultValue="")	String dateov,
			@RequestParam(name="datef",defaultValue="")	String datefr,
			@RequestParam(name="nom",defaultValue="")	String nom,
			@RequestParam(name="err",defaultValue="0")	String err
	){             
 		Page<Dossier> dossiers = null;
 		Date dateo = null, datef = null;
		model.addAttribute("nom", nom);
 		nom = "%"+nom+"%"; 
 		
 		SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd"); 
 		try{ if(!dateov.isEmpty()) dateo = mdyFormat.parse(dateov);  }catch(Exception e){} 
 		try{ if(!datefr.isEmpty()) datef = mdyFormat.parse(datefr);  }catch(Exception e){} 
 		 
		if(type.equals("f"))
		{ 
			if(dateo!=null) 
			{
				if(datef!=null) 
				{  
					dossiers = metierDossier.getDossiersByDateCreationFermeture(nom, dateo, datef, p,s);
				} 
				else dossiers = metierDossier.getDossiersClosedByDateCreation(nom, dateo,  p,s);
			}  
			else if(datef!=null) 
			{  
				dossiers = metierDossier.getDossiersByDateFermeture(nom, datef, p,s);
			} 
			else dossiers = metierDossier.getDossiersClosed(nom, p,s);
		} 
		else if(type.equals("nf"))
		{ 
			if(dateo!=null) 
			{ 
				dossiers = metierDossier.getDossiersNotClosedByDateCreation(nom, dateo, p, s);
			}   
			else dossiers = metierDossier.getDossiersNotClosed(nom, p, s);
		} 
		else if(dateo!=null) 
		{
			if(datef!=null) 
			{  
				dossiers = metierDossier.getDossiersByDateCreationFermeture(nom, dateo, datef, p,s);
			} 
			else dossiers = metierDossier.getDossiersByDateCreation(nom, dateo, p,s);
		}  
		else if(datef!=null) 
		{  
			dossiers = metierDossier.getDossiersByDateFermeture(nom, datef, p,s);
		} 
		else dossiers = metierDossier.getDossiers(nom, p, s); 
		 
		model.addAttribute("type", type);
		model.addAttribute("dateo", dateov);
		model.addAttribute("datef", datefr);
		model.addAttribute("dossiers", dossiers.getContent());

		
		model.addAttribute("page", p);
		model.addAttribute("size", s);
		model.addAttribute("pages", new int[dossiers.getTotalPages()]); 
		model.addAttribute("pageCourant", p); 
		
		if(err.equals("1"))
			model.addAttribute("selectFolder",true);
		
		return "dossiers"; 
	}  
	
	@RequestMapping(value="/dossiers/save",method=RequestMethod.POST)
	public String saveDossier(@Valid Dossier dos, BindingResult result,  Model model) 
	{    
		if(result.hasErrors()) { 
			if(dos.getNumero() == null)
			{
				model.addAttribute("addFailed",true);
				return index(model,0,20,"t","","","","");
			}
			
			model.addAttribute("updateFailed",true);
			model.addAttribute("dos",dos);
			return index(model,0,20,"t","","","","");
		}
		 
		if(dos.getNumero() == null) {
			dos.setDateCreation(LocalDateTime.now());
			model.addAttribute("addOk",true);
		}
		else model.addAttribute("updateOk",true); 
		
		User u = metierUser.getUser(
			SecurityContextHolder.getContext().getAuthentication().getName()
		);
		dos.setUser(u);
		dos = metierDossier.saveDossier(dos);
		model.addAttribute("dos",dos);
		return index(model,0,20,"","","","",""); 
	}
	
	@RequestMapping(value="/dossiers/delete", method=RequestMethod.POST)
	public String deleteDossier(Model model,@RequestParam(name="num",defaultValue="0")Long num) 
	{   
		metierDossier.deleteDossier(num);
		model.addAttribute("deleteOk","Dossier "+num+" supprimé !");
		return index(model,0,20,"","","","",""); 
	}
	
	@RequestMapping(value="/dossiers/open")
	public String opendossier(Model model, @RequestParam(name="num")Long num) 
	{   
		Dossier dos = metierDossier.getDossier(num);
		if(dos == null)  return "redirect:/dossiers"; 
		session.setAttribute("dossier", dos);
		return "redirect:/commandes";
	} 
	
	@RequestMapping(value="/dossiers/fermer")
	public String fermerdossier() 
	{    
		session.setAttribute("dossier", null);
		session.removeAttribute("dossier");
		return "redirect:/dossiers"; 
	} 
	
	@RequestMapping(value="/dossiers/details")
	public String showdossier(Model model, @RequestParam(name="num", defaultValue="0")Long num) 
	{   
		Dossier dos = metierDossier.getDossier(num);
		if(dos == null)  return "redirect:/dossiers"; 
		model.addAttribute("dos",dos);
		model.addAttribute("nbrCmd",metierCmd.getNombreCommandes(num));
		model.addAttribute("nbrFct",metierFct.getNombreFactures(num));
		model.addAttribute("prixa",metierFct.getPrixAchats(num));
		model.addAttribute("prixv",metierFct.getPrixVentes(num));
		model.addAttribute("nbra",metierFct.getNombreAchats(num));
		model.addAttribute("nbrv",metierFct.getNombreVentes(num));
		return index(model,0,20,"","","","","");
	} 
	/* ----------------------------------- */
	
	/*@PostMapping("/dossiers")
	public String creerDossier(@RequestParam("nom") String nom) {
	    Dossier dossier = new Dossier();
	    dossier.setNom(nom);
	    dossier.setDateCreation(LocalDateTime.now());
	    dossierRepository.save(dossier);
	    
	    // Redirect to the appropriate page
	    return "redirect:/dossiers";
	}*/
	/* @GetMapping("/dossiers")
	    public String afficherFormulaire(Model model) {
	        model.addAttribute("dossier", new Dossier());
	        
	        return "dossiers";
	    }*/
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
