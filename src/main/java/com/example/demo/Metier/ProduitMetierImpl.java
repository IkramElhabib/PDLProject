package com.example.demo.Metier;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.IMetier.IProduitMetier;
import com.example.demo.dao.ProduitRepository;
import com.example.demo.entities.Produit;

@Service
public class ProduitMetierImpl implements IProduitMetier
{ 
	@Autowired
	private ProduitRepository prdRep;

	@Override
	public Produit getProduit(String ref) { 
		return prdRep.getById(ref);
	}

	/*@Override
	public Page<Produit> getProduitsByMotCle(String mc,  int page, int size) {
		return prdRep.findAllByMotCle(mc,PageRequest.of(page, size));
	}*/

	@Override
	public Produit saveProduit(Produit produit) {
		return prdRep.save(produit);
	}

	@Override
	public boolean deleteProduit(String ref) {
		Produit prd = prdRep.getById(ref);
		if(prd!=null) {
			prdRep.delete(prd);
			return true;
		}
		return false;
	}


	@Override
	public List<Produit> getProduits() {
		// TODO Auto-generated method stub
		return prdRep.findAll();
	}

	@Override
	public Page<Produit> getProduitsByMotCle(String mc, int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}
}