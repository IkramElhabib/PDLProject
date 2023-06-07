package com.example.demo.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dao.ClientRepository;
import com.example.demo.dao.CommandeRepository;
import com.example.demo.dao.FournisseurRepository;
import com.example.demo.dao.LcRepository;
import com.example.demo.dao.ProduitRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Commande;
import com.example.demo.entities.LigneCommande;
import com.example.demo.entities.Produit;

import jakarta.validation.Valid;


@Controller
public class CommandesController {
	
	public CommandeRepository commandeRepository;
	public FournisseurRepository fournisseurRepository;
	public ClientRepository clientRepository;
	public ProduitRepository produitRepository;
	public LcRepository lcRepository;

    
    public CommandesController(CommandeRepository commandeRepository, FournisseurRepository fournisseurRepository, ClientRepository clientRepository, ProduitRepository produitRepository, LcRepository lcRepository) {
        this.commandeRepository = commandeRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.clientRepository = clientRepository;
        this.produitRepository = produitRepository;
        this.lcRepository = lcRepository;
    }
    
    @PostMapping("/vente")
    public ResponseEntity<Map<String, Object>> saveCommandeVente(@ModelAttribute @Valid Commande commande, BindingResult bindingResult, Model model) {
        // Vérifications de validation de commande

        if (bindingResult.hasErrors()) {
            // Si des erreurs de validation sont présentes, retourner les erreurs
            Map<String, Object> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            return ResponseEntity.badRequest().body(errors);
        }

        // Récupérer le client associé à la commande de vente
        Client client = clientRepository.findByCode(commande.getClient().getCode());
        // Ajouter le client à la commande
        commande.setClient(client);

        // Définir la valeur par défaut pour "valide" (false)
        commande.setValide(false);

        commandeRepository.save(commande);

        // Créer une réponse JSON avec le numéro de commande
        Map<String, Object> response = new HashMap<>();
        response.put("numero", commande.getNumero());

        return ResponseEntity.ok(response);
    }


    
    @GetMapping("/vente/formulaire")
    public String afficherFormulaireVente(Model model, @RequestParam(value = "numero", required = false) Integer numero) {
        List<Client> clients = clientRepository.findAll();
        List<Produit> produits = produitRepository.findAll();
        model.addAttribute("clients", clients);

        if (numero != null) {
            Commande commande = commandeRepository.findByNumero(numero);
            model.addAttribute("commande", commande);
        }

        model.addAttribute("produits", produits);
        model.addAttribute("ligneCommande", new LigneCommande());
        model.addAttribute("commande", new Commande());

        return "formulaire-nouvelle-commande";
    }

    @PostMapping("/ligneCommande/ajouter")
    public String ajouterLigneCommande(@ModelAttribute LigneCommande ligneCommande) {
        // Récupérer la commande associée à la ligne de commande
        Commande commande = ligneCommande.getCommande();
        
        // Récupérer le produit associé à la ligne de commande
        Produit produit = produitRepository.findById(ligneCommande.getProduit().getRef()).orElse(null);
        
        if (produit != null) {
            // Mettre à jour le produit de la ligne de commande avec l'instance persistée
            ligneCommande.setProduit(produit);
            ligneCommande.setCommande(commande);
            // Enregistrer la ligne de commande
            lcRepository.save(ligneCommande);
        } else {
            // Gérer le cas où le produit n'est pas trouvé
        }
        
        // Rediriger vers la page des détails de la commande
        return "formulaire-nouvelle-commande";
    }




    /*@PostMapping
    public String saveCommandeVente(@ModelAttribute("commande") Commande commande, @ModelAttribute("ligneCommande") LigneCommande ligneCommande) {
        // Récupérer le client associé à la commande de vente
        Client client = clientRepository.findByCode(commande.getClient().getCode());
        // Ajouter le client à la commande
        commande.setClient(client);
        
        // Définir la valeur par défaut pour "valide" (false)
        commande.setValide(false);
        
        // Enregistrer la commande
        commande = commandeRepository.save(commande);
        
        // Récupérer le produit associé à la ligne de commande
        Produit produit = produitRepository.findById(ligneCommande.getProduit().getRef()).orElse(null);
        // Ajouter le produit à la ligne de commande
        ligneCommande.setProduit(produit);
        
        // Associer la ligne de commande à la commande
        ligneCommande.setCommande(commande);
        
        // Enregistrer la ligne de commande
        lcRepository.save(ligneCommande);
        
        return "redirect:/commandes/vente/details/" + commande.getNumero();
    }*/
    
    @GetMapping("/details/{numero}")
    public String afficherDetailsCommande(@PathVariable("numero") int numero, Model model) {
        Commande commande = commandeRepository.findByNumero(numero);
        model.addAttribute("commande", commande);
        return "details-commande";
    }


}