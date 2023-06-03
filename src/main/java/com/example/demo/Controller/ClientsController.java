package com.example.demo.Controller;

import java.util.List;

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

import com.example.demo.IMetier.IClientMetier;
import com.example.demo.dao.ClientRepository;
import com.example.demo.dao.FournisseurRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Fournisseur;

import jakarta.validation.Valid;

@Controller
public class ClientsController {
	@Autowired
	private IClientMetier metierClient;
	
	private final ClientRepository clientRepository;
	
	 public ClientsController(ClientRepository clientRepository) {
	        this.clientRepository = clientRepository;
	    }
	
	
	@RequestMapping(value= {"/clients"})
	public String index
		( 
			Model model,
			@RequestParam(name="page",defaultValue="0")int p,
			@RequestParam(name="size",defaultValue="8")int s,
			@RequestParam(name="mc",defaultValue="")String mc
		) 
	{
		Page<Client> clients = metierClient.getClientsByMotCle("%"+mc+"%", p, s);
		model.addAttribute("clients", clients.getContent());
		model.addAttribute("pages", new int[clients.getTotalPages()]);
		model.addAttribute("size", s);
		model.addAttribute("pageCourant", p);
		model.addAttribute("mc", mc); 
		
		if(!model.containsAttribute("client"))
		model.addAttribute("client", new Client()); 
		
		return "clients"; 
	}
	
	

	@RequestMapping(value= {"/clients/add"}, method=RequestMethod.POST)
	public String addClient(@Valid Client client, BindingResult result, Model model) 
	{    	
		metierClient.getClient(client.getCode());
		if( metierClient.getClient(client.getCode())!=null )
			model.addAttribute("dejaExist", true);
		
		if(saveClient(client,result,model))  
			model.addAttribute("addOk","Client ajouté !");
		else model.addAttribute("addFailed",true); 
		
		return index(model,0,8,"");
	}
	
	
	@RequestMapping(value="/clients/update",method=RequestMethod.POST)
	public String updateClient(@Valid Client client, BindingResult result, Model model) 
	{    
		if(saveClient(client,result,model)) 
			model.addAttribute("updateOk","Client "+client.getCode()+" est Mis à jour!");
		else model.addAttribute("updateFailed",true);
		
		return index(model,0,8,"");
	} 
	
	private boolean saveClient(Client client, BindingResult result, Model model)
	{
		if (result.hasErrors()) 
		{
			model.addAttribute("client", client);
			return false;
		}

		metierClient.saveClient(client);
		return true;
	}
	
 
	@RequestMapping(value="/clients/delete")
	public String deleteClient(Model model,@RequestParam(name="code",defaultValue="0")String code) 
	{  
		metierClient.deleteClient(code);
		model.addAttribute("deleteOk","Client "+code+" est supprimé");
		return index(model,0,8,"");
	}
	
	
	
	
	
	@RequestMapping(value="/clients/get", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody Client getroduit(@RequestParam(name="code")String code) 
	{  
		Client frs = metierClient.getClient(code);  
		return frs;
	}
	
	 @GetMapping("/client/add")
	    public String ajouterClient(Model model) {
		 model.addAttribute("client", new Client());
	        return "ajoutClient";
	    }
	 
	 @PostMapping("/clientadd")
	 public String ajouterC(@ModelAttribute @Valid Client client, BindingResult bindingResult, Model model) {
	     if (bindingResult.hasErrors()) {
	         // If there are validation errors, return to the form with the error messages
	         return "ajoutClient";
	     }
	     Client existingClients = clientRepository.findByCode(client.getCode());
	     if (existingClients != null) {
	         // Add a field error for the "code" field
	         bindingResult.rejectValue("code", "error.client", "Le code existe déjà. Veuillez la changer.");
	         return "ajoutClient";
	     }

	     clientRepository.save(client);

	     // Add success message to the model
	     model.addAttribute("successMessage", "Client ajouté avec succès.");

	     return "redirect:/listClients";
	 }
	 
	 @GetMapping("/listClients")
	    public String afficherListeClient(Model model) {
	        List<Client> clients = clientRepository.findAll();
	        model.addAttribute("clients", clients);
	        return "listClients";
	    }
	 @GetMapping("/client/{code}")
	 public String afficherClient(@PathVariable("code") String code, Model model) {
	     Client client = clientRepository.findByCode(code);
	     if (client !=null) {
	         model.addAttribute("client", client);
	         return "updateClient";
	     } else {
	         // Le client n'a pas été trouvé, rediriger ou afficher un message d'erreur
	         return "redirect:/listClients";
	     }
	 }
	 
	 @PostMapping("/clientupdate/{code}")
	    public String updateC(@PathVariable String code, @RequestParam String nom, @RequestParam String email, @RequestParam String prenom, @RequestParam String adresse, @RequestParam int age, @RequestParam String tel, Model model, RedirectAttributes redirectAttributes) {
	        clientRepository.updateClient(code, nom, email, prenom, adresse, age, tel);
	        redirectAttributes.addFlashAttribute("successMessage", "Le client a été modifiée avec succès !");
	        return "redirect:/listClients";
	    }
	 
	 @GetMapping("/deleteClients/{code}")
	    public String deleteC(@PathVariable("code") String code) {
	        clientRepository.deleteById(code);
	        return "redirect:/listClients";
	    }
	
}
