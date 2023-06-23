package com.example.demo.Controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.IMetier.IFournisseurMetier;
import com.example.demo.dao.FournisseurRepository;
import com.example.demo.entities.Fournisseur;
import com.example.demo.entities.Produit;

import jakarta.validation.Valid;



@Controller
public class FournisseurController {
	
	@Autowired
	private IFournisseurMetier metierFournisseur;
	private final FournisseurRepository fournisseurRepository;
	
	 public FournisseurController(FournisseurRepository fournisseurRepository) {
	        this.fournisseurRepository = fournisseurRepository;
	    }
	
	@RequestMapping(value= {"/fournisseurs"})
	public String index
		( 
			Model model,
			@RequestParam(name="page",defaultValue="0")int p,
			@RequestParam(name="size",defaultValue="8")int s,
			@RequestParam(name="mc",defaultValue="")String mc
		) 
	{
		Page<Fournisseur> fournisseurs = metierFournisseur.getFournisseursByMotCle("%"+mc+"%",p,s );
		model.addAttribute("fournisseurs", fournisseurs.getContent());
		model.addAttribute("pages", new int[fournisseurs.getTotalPages()]);
		model.addAttribute("size", s);
		model.addAttribute("pageCourant", p);
		model.addAttribute("mc", mc); 
		
		if(!model.containsAttribute("fournisseur"))
		model.addAttribute("fournisseur", new Fournisseur()); 
		
		return "fournisseurs"; 
	}
	
	@RequestMapping(value= {"/fournisseurs/add"}, method=RequestMethod.POST)
	public String addFournisseur(@Valid Fournisseur fournisseur, BindingResult result, Model model) 
	{    	
		metierFournisseur.getFournisseur(fournisseur.getCode());
		if( metierFournisseur.getFournisseur(fournisseur.getCode())!=null )
			model.addAttribute("dejaExist", true);
		
		else if(saveFournisseur(fournisseur,result,model))  
			model.addAttribute("addOk","Fournisseur ajouté !");
		else model.addAttribute("addFailed",true); 
		
		return index(model,0,8,"");
	}
	@RequestMapping(value="/fournisseurs/update",method=RequestMethod.POST)
	public String updateFournisseur(@Valid Fournisseur fournisseur, BindingResult result, Model model) 
	{    
		if(saveFournisseur(fournisseur,result,model)) 
			model.addAttribute("updateOk","Fournisseur "+fournisseur.getCode()+" est Mis à jour!");
		else model.addAttribute("updateFailed",true); 
		
		return index(model,0,8,"");
	} 
	
	private boolean saveFournisseur(Fournisseur fournisseur, BindingResult result, Model model)
	{
		if (result.hasErrors()) 
		{
			System.err.println(result.getAllErrors());
			model.addAttribute("fournisseur", fournisseur); 
			return false;
		}

		metierFournisseur.saveFournisseur(fournisseur);
		return true;
	}
	
	@RequestMapping(value="/fournisseurs/delete")
	public String deleteFournisseur(Model model,@RequestParam(name="code",defaultValue="0")String code) 
	{  
		metierFournisseur.deleteFournisseur(code);
		model.addAttribute("deleteOk","Fournisseur "+code+" est supprimé");
		return index(model,0,8,"");
	}
	
	@RequestMapping(value="/fournisseurs/get", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody Fournisseur getroduit(@RequestParam(name="code")String code) 
	{  
		Fournisseur frs = metierFournisseur.getFournisseur(code);  
		return frs;
	}
	 @GetMapping("/fournisseur/add")
	    public String ajouterFournisseur(Model model) {
		 model.addAttribute("fournisseur", new Fournisseur());
	        return "ajoutFournisseur";
	    }
	 
	 @PostMapping("/fournisseuradd")
	 public String ajouterFournisseur(@ModelAttribute @Valid Fournisseur fournisseur, BindingResult bindingResult, Model model) {
	     if (bindingResult.hasErrors()) {
	         // If there are validation errors, return to the form with the error messages
	         return "listProduits";
	     }
	     Fournisseur existingFournisseur = fournisseurRepository.findByCode(fournisseur.getCode());
	     if (existingFournisseur != null) {
	         // Add a field error for the "code" field
	         bindingResult.rejectValue("code", "error.fournisseur", "La référence existe déjà. Veuillez la changer.");
	         return "ajoutFournisseur";
	     }

	     fournisseurRepository.save(fournisseur);

	     // Add success message to the model
	     model.addAttribute("successMessage", "Fournisseur ajouté avec succès.");

	     return "redirect:/listFournisseur";
	 }
	 
	 @GetMapping("/listFournisseur")
	    public String afficherListeFournisseur(Model model) {
	        List<Fournisseur> fournisseurs = fournisseurRepository.findAll();
	        model.addAttribute("fournisseurs", fournisseurs);
	        return "listFournisseurs";
	    }
	 @GetMapping("/fournisseur/{code}")
	 public String afficherFournisseur(@PathVariable("code") String code, Model model) {
	     Fournisseur fournisseur = fournisseurRepository.findByCode(code);
	     if (fournisseur !=null) {
	         model.addAttribute("fournisseur", fournisseur);
	         return "updatefournisseur";
	     } else {
	         // Le fournisseur n'a pas été trouvé, rediriger ou afficher un message d'erreur
	         return "redirect:/listFournisseur";
	     }
	 }
	 
	 @PostMapping("/fournisseur/{code}")
	    public String updateF(@PathVariable String code, @RequestParam String nom, @RequestParam String email, @RequestParam String raisonSociale, @RequestParam String adresse, @RequestParam Double capital, @RequestParam String tel, @RequestParam String fax, Model model, RedirectAttributes redirectAttributes) {
	        fournisseurRepository.updateFournisseur(code, nom, email, raisonSociale, adresse, capital, tel, fax);
	        redirectAttributes.addFlashAttribute("successMessage", "Le fournisseur a été modifiée avec succès !");
	        return "redirect:/listFournisseur";
	    }
	 
	 @GetMapping("/deleteFournisseur/{code}")
	    public String deleteFourn(@PathVariable("code") String code) {
	        fournisseurRepository.deleteById(code);
	        return "redirect:/listFournisseur";
	    }
	

	
	

}