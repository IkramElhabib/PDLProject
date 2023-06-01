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

import com.example.demo.IMetier.IProduitMetier;
import com.example.demo.IMetier.ITvaMetier;
import com.example.demo.dao.ProduitRepository;
import com.example.demo.entities.Produit;


import jakarta.validation.Valid;


@Controller
public class ProduitController {
	
	@Autowired private IProduitMetier metierProduit;
	@Autowired private ITvaMetier metierTva;
	@Autowired ProduitRepository produitRepository;
	
	// GET -------------------------------------------------------------
	@RequestMapping(value= {"/produits"})
	public String index( 
			Model model,
			@RequestParam(name="page",defaultValue="0")Integer p,
			@RequestParam(name="size",defaultValue="8")Integer s,
			@RequestParam(name="mc",defaultValue="")String mc
	){
		Page<Produit> prds = metierProduit.getProduitsByMotCle("%"+mc+"%",p,s );
		model.addAttribute("produits", prds.getContent());
		model.addAttribute("pages", new int[prds.getTotalPages()]);
		model.addAttribute("size", s);
		model.addAttribute("pageCourant", p);
		model.addAttribute("mc", mc);  
		
		model.addAttribute("tvas",metierTva.getTvas());
		
		if(!model.containsAttribute("produit"))
		model.addAttribute("produit", new Produit()); 
		
		return "listProduits"; 
	}
	
	 @GetMapping("/produits/add")
	    public String afficherFormulaireAjoutProd(Model model) {
	        model.addAttribute("produit", new Produit());
	        return "ajoutProduit";
	    }
	 @PostMapping("/addProd")
	 public String ajouterProduit(@ModelAttribute @Valid Produit produit, BindingResult bindingResult, Model model) {
	     if (bindingResult.hasErrors()) {
	         // If there are validation errors, return to the form with the error messages
	         return "ajoutProduit";
	     }

	     Optional<Produit> existingProduit = produitRepository.findByRef(produit.getRef());
	     if (existingProduit.isPresent()) {
	         // Add a field error for the "ref" field
	         bindingResult.rejectValue("ref", "error.produit", "La référence existe déjà. Veuillez la changer.");
	         return "ajoutProduit";
	     }

	     produitRepository.save(produit);

	     // Add success message to the model
	     model.addAttribute("successMessage", "Produit ajouté avec succès.");

	     return "redirect:/produits/add";
	 }

	 
	 @GetMapping("/listProd")
	    public String afficherListeProduits(Model model) {
	        List<Produit> produits = produitRepository.findAll();
	        model.addAttribute("produits", produits);
	        return "listProduits";
	    }

	/*@RequestMapping(value= {"/produits/add"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String addProduit(@Valid Produit produit, BindingResult result, Model model) 
	{    	
		metierProduit.getProduit(produit.getRef());
		if( metierProduit.getProduit(produit.getRef())==null )
		{
			if(saveProduit(produit,result,model))  
				model.addAttribute("addOk","Produit ajouté !");
			else model.addAttribute("addField","");
		}
		else {
			model.addAttribute("dejaExist", true);
			model.addAttribute("addField","");
		}
		
		return index(model,0,8,"");
	}*/
	 
	 @GetMapping("/produit/{ref}")
	 public String afficherProduit(@PathVariable("ref") String ref, Model model) {
	     Optional<Produit> produit = produitRepository.findByRef(ref);
	     if (produit.isPresent()) {
	         model.addAttribute("produit", produit.get());
	         return "updateproduit";
	     } else {
	         // Le produit n'a pas été trouvé, rediriger ou afficher un message d'erreur
	         return "redirect:/listProd";
	     }
	 }
	 
	 @PostMapping("/produit/{ref}")
	    public String updateProd(@PathVariable String ref, @RequestParam String designation, @RequestParam double prix, @RequestParam int quantite, @RequestParam int quantiteAlert, Model model, RedirectAttributes redirectAttributes) {
	        produitRepository.updateProduit(ref, designation, prix, quantite, quantiteAlert);
	        redirectAttributes.addFlashAttribute("successMessage", "Le produit a été modifiée avec succès !");
	        return "redirect:/listProd";
	    }


	
	/*@RequestMapping(value="/produits/update",method=RequestMethod.POST)
	public String updateProduit(@Valid Produit produit, BindingResult result, Model model) 
	{    
		if(saveProduit(produit,result,model)) 
			model.addAttribute("updateOk","Produit "+produit.getRef()+" est Mis à jour!");
		else model.addAttribute("updateField","");
		model.addAttribute("produit",produit); 
		return index(model,0,8,"");
	} */
	
	/* @GetMapping("/updateform")
	    public String showProduit(@PathVariable String ref, Model model) {
	        Optional<Produit> produit = produitRepository.findByRef(ref);
	        if (produit.isPresent()) {
	            model.addAttribute("produit", produit.get());
	            return "formupdate";
	        } else {
	            return "ajoutProduit";
	        }
	    }*/
	    
	   
	
	private boolean saveProduit(Produit produit, BindingResult result, Model model)
	{
		if (result.hasErrors()) 
		{
			model.addAttribute("produit", produit); 
			return false;
		}

		metierProduit.saveProduit(produit);
		return true;
	}
	

	@RequestMapping(value="/produits/delete")
	public String deleteProduit(Model model,@RequestParam(name="ref",defaultValue="0")String ref) 
	{  
		metierProduit.deleteProduit(ref);
		model.addAttribute("deleteOk","Produit "+ref+" est supprimé");
		return index(model,0,8,"");
	}
	
	@RequestMapping(value="/produits/get", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody Produit getproduit(@RequestParam(name="ref")String ref) 
	{  
		Produit p = metierProduit.getProduit(ref); 
		return p;
	}
	
	  @GetMapping("/deleteProd/{ref}")
	    public String delProd(@PathVariable("ref") String ref) {
	        produitRepository.deleteById(ref);
	        return "redirect:/listProd";
	    }
	
	
	

}
