package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.dao.CommandeRepository;
import com.example.demo.dao.LcRepository;
import com.example.demo.dao.ProduitRepository;
import com.example.demo.entities.Commande;
import com.example.demo.entities.Produit;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class Commandes2Controller {
	
	@Autowired
    private CommandeRepository commandeRepository;
	@Autowired
	private ProduitRepository produitRepository;

    @Autowired
    private LcRepository lcRepository;

   
   
}
