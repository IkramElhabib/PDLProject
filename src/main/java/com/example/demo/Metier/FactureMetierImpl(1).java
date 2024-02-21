package com.example.demo.Metier;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.IMetier.IFactureMetier;
import com.example.demo.dao.FactureRepository;
import com.example.demo.entities.Facture;

@Service

public class FactureMetierImpl implements IFactureMetier
{

	@Autowired private FactureRepository fctRep; 
	
	@Override
	public List<Facture> getFactures() { 
		return fctRep.findAll();
	}

	@Override
	public Page<Facture> getFactures(Long numDossier,int page, int size) { 
		return fctRep.findAll(PageRequest.of(page, size));
	}
 
	@Override
	public Facture saveFacture(Facture commande) { 
		return fctRep.save(commande);
	}
	 

	@Override
	public Page<Facture> getFacturesClients(Long numDossier, int page, int size) { 
		return fctRep.findAllFacturesClients(numDossier,PageRequest.of(page, size) );
	}

	@Override
	public Page<Facture> getFacturesFournisseurs(Long numDossier,int page, int size) { 
		return fctRep.findAllFacturesFournisseurs(numDossier,PageRequest.of(page, size));
	}

	@Override
	public Page<Facture> getFacturesClients(Long numDossier,Date date, int page, int size) { 
		return fctRep.findAllFacturesClients(numDossier,date, PageRequest.of(page, size));
	}

	@Override
	public Page<Facture> getFacturesFournisseurs(Long numDossier,Date date, int page, int size) { 
		return fctRep.findAllFacturesFournisseurs(numDossier,date, PageRequest.of(page, size));
	}

	@Override
	public Page<Facture> getFacturesOfClient(Long numDossier,String code, int page, int size) { 
		return fctRep.findAllFacturesOfClient(numDossier,code, PageRequest.of(page, size));
	}

	@Override
	public Page<Facture> getFacturesOfFournisseur(Long numDossier,String code, int page, int size) { 
		return fctRep.findAllFacturesOfFournisseur(numDossier,code, PageRequest.of(page, size));
	}

	@Override
	public Page<Facture> getFacturesOfClient(Long numDossier,String code, Date date, int page, int size) { 
		return fctRep.findAllFacturesOfClient(numDossier,code, date, PageRequest.of(page, size));
	}

	@Override
	public Page<Facture> getFacturesOfFournisseur(Long numDossier,String code, Date date, int page, int size) { 
		return fctRep.findAllFacturesOfFournisseur(numDossier,code, date, PageRequest.of(page, size));
	} 
  

	@Override
	public Facture getFacture(Long num) {
		return fctRep.find(num);
	}

	@Override
	public boolean deleteFacture(Long num) {
		Facture fct = fctRep.find(num); if(fct==null) return false; fctRep.delete(fct); return true;
	}

	
	
	@Override
	public int getNombreFactures(Long numDossier) { 
		return fctRep.count(numDossier);
	}

	@Override
	public int getNombreAchats(Long numDossier) { 
		return fctRep.countAchatsProduits(numDossier);
	}

	@Override
	public int getNombreVentes(Long numDossier) { 
		return fctRep.countVentesProduits(numDossier);
	}

	@Override
	public Double getPrixAchats(Long numDossier) { 
		return fctRep.prixAchatsProduits(numDossier);
	}

	@Override
	public Double getPrixVentes(Long numDossier) { 
		return fctRep.prixVentesProduits(numDossier);
	}


	@Override
	public Double getRevenues(Date d1, Date d2) {
		return 0d;//fctRep.countRevenues(d1,d2);
	}

	@Override
	public Integer getQteVentes(Date d1, Date d2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getSVentes(Long numDossier, Date d1, Date d2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getSVenteProduit(Long numDossier, Date d1, Date d2, String ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getSAchats(Long numDossier, Date d1, Date d2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getSAchatsProduit(Long numDossier, Date d1, Date d2, String ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getSPrixVentes(Long numDossier, Date d1, Date d2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getSPrixVenteProduit(Long numDossier, Date d1, Date d2, String p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getSPrixAchats(Long numDossier, Date d1, Date d2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getSPrixAchatsProduit(Long numDossier, Date d1, Date d2, String p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> getSProduitsAchete(Long numDossier, Date d1, Date d2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> getSProduitsVendu(Long numDossier, Date d1, Date d2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getPrixVentes(Date d1, Date d2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getPrixAchats(Date d1, Date d2) {
		// TODO Auto-generated method stub
		return null;
	}

 
}

