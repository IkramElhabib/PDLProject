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
import com.example.demo.dao.DossierRepository;
import com.example.demo.dao.FournisseurRepository;
import com.example.demo.dao.LcRepository;
import com.example.demo.dao.ProduitRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Commande;
import com.example.demo.entities.Dossier;
import com.example.demo.entities.Fournisseur;
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
	public DossierRepository dossierRepository;

    
    public CommandesController(CommandeRepository commandeRepository, FournisseurRepository fournisseurRepository, ClientRepository clientRepository, ProduitRepository produitRepository, LcRepository lcRepository, DossierRepository dossierRepository) {
        this.commandeRepository = commandeRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.clientRepository = clientRepository;
        this.produitRepository = produitRepository;
        this.lcRepository = lcRepository;
        this.dossierRepository = dossierRepository;
    }
    
    @PostMapping("/vente")
    public String saveCommandeVente(@ModelAttribute @Valid Commande commande, BindingResult bindingResult, Model model) {
        // Validation checks for the commande object

        if (bindingResult.hasErrors()) {
            // Handle validation errors
            Map<String, Object> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            model.addAttribute("errors", errors);
            return "formulaire-nouvelle-commande";
        }

        if (commande.getClient() != null && commande.getFournisseur() != null) {
            // Handle the case where both a client and a fournisseur are selected
            model.addAttribute("error", "Veuillez sélectionner soit un client soit un fournisseur, mais pas les deux.");
            return "formulaire-nouvelle-commande";
        }

        if (commande.getClient() != null) {
            Client client = clientRepository.findByCode(commande.getClient().getCode());
            if (client == null) {
                model.addAttribute("error", "Client introuvable");
                return "formulaire-nouvelle-commande";
            }
            commande.setClient(client);
            commande.setFournisseur(null);
        }

        if (commande.getFournisseur() != null) {
            Fournisseur fournisseur = fournisseurRepository.findByCode(commande.getFournisseur().getCode());
            if (fournisseur == null) {
                model.addAttribute("error", "Fournisseur introuvable");
                return "formulaire-nouvelle-commande";
            }
            commande.setFournisseur(fournisseur);
            commande.setClient(null);
        }
        // Récupérer le dossier sélectionné par son ID
        Long dossierId = commande.getDossier().getNumero();
        Dossier dossier = dossierRepository.findById(dossierId).orElse(null);

        // Associer le dossier à la facture
        commande.setDossier(dossier);
        

        commande.setValide(false);
        commandeRepository.save(commande);

        // Redirect to the formulaire for adding ligne de commande
        return "redirect:/ligne/form?numero=" + commande.getNumero();
    }


    
    @GetMapping("/vente/formulaire")
    public String afficherFormulaireVente(Model model, @RequestParam(value = "numero", required = false) Integer numero) {
        List<Client> clients = clientRepository.findAll();
        List<Produit> produits = produitRepository.findAll();
        List<Fournisseur> fournisseurs= fournisseurRepository.findAll();
        List<Dossier> dossiers = dossierRepository.findByDateFermetureIsNull();

        model.addAttribute("fournisseurs", fournisseurs);
        model.addAttribute("clients", clients);
        model.addAttribute("dossiers", dossiers);


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
    public String ajouterLigneCommande(@ModelAttribute LigneCommande ligneCommande, @RequestParam("numero") Integer numero, RedirectAttributes redirectAttributes) {
        // Récupérer la commande associée au numéro
        Commande commande = commandeRepository.findByNumero(numero);

        if (commande != null) {
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

            redirectAttributes.addAttribute("numero", numero);
            return "redirect:/vente/formulaire";
        } else {
            // Gérer le cas où la commande n'est pas trouvée
        }

        return "redirect:/vente/formulaire";
    }


    
    @GetMapping("/ligne/form")
    public String afficherFormulaireLigne(Model model, @RequestParam(value = "numero", required = false) Integer numero) {
        List<Client> clients = clientRepository.findAll();
        List<Produit> produits = produitRepository.findAll();
        model.addAttribute("clients", clients);

        if (numero != null) {
            Commande commande = commandeRepository.findByNumero(numero);
            model.addAttribute("commande", commande);
            model.addAttribute("ligneCommande", new LigneCommande());
        }

        model.addAttribute("produits", produits);

        return "formulaire-nouvelle-commande";
    }

    
    @GetMapping("/commandeslist")
    public String afficherCommandes(Model model) {
        List<Commande> commandes = commandeRepository.findAll();
        model.addAttribute("commandes", commandes);
        return "listcommandes";
    }
    
    @GetMapping("/ventelist")
    public String afficherVentes(Model model) {
        List<Commande> commandes = commandeRepository.findByClientIsNotNullAndFournisseurIsNull();
        model.addAttribute("commandes", commandes);
        return "listcommandes";
    }

    @GetMapping("/achatlist")
    public String afficherAchats(Model model) {
        List<Commande> commandes = commandeRepository.findByClientIsNullAndFournisseurIsNotNull();
        model.addAttribute("commandes", commandes);
        return "listcommandes";
    }





}