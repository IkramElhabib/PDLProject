package com.example.demo.Metier;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.IMetier.IDossierMetier;
import com.example.demo.dao.DossierRepository;
import com.example.demo.entities.Dossier;

@Service
public class DossierMetierImpl implements IDossierMetier
{ 
	@Autowired
	private DossierRepository dosRep;
  

	@Override
	public Dossier getDossier(Long num) { 
		return dosRep.getOne(num);
	}

	@Override
	public Dossier saveDossier(Dossier dos) { 
		return dosRep.save(dos);
	}

	@Override
	public boolean deleteDossier(Long num) { 
		Dossier dos = dosRep.getOne(num);
		if(dos==null) return false;
		dosRep.delete(dos);
		return true;
	}

	@Override
	public Page<Dossier> getDossiers(String nom, int page, int size) { 
		return dosRep.findAll(nom, PageRequest.of(page, size));
	}

	@Override
	public Page<Dossier> getDossiersByDateFermeture(String nom, Date dateFermeture, int page, int size) {
		return dosRep.findAllByDateFermeture(nom,dateFermeture, PageRequest.of(page, size));
	}

	@Override
	public Page<Dossier> getDossiersByDateCreation(String nom, Date dateCreation, int page, int size) {
		return dosRep.findAllByDateCreation(nom, dateCreation, PageRequest.of(page, size));
	}

	@Override
	public Page<Dossier> getDossiersByDateCreationFermeture(String nom, Date dateCreation, Date dateFermeture, int page,
			int size) {
		return dosRep.findAllByDateCreationFermeture(nom, dateCreation, dateFermeture, PageRequest.of(page, size));
	}

	@Override
	public Page<Dossier> getDossiersClosedByDateCreation(String nom, Date dateCreation, int page, int size) {
		return dosRep.findAllClosedByDateCreation(nom,dateCreation, PageRequest.of(page, size));
	}

	@Override
	public Page<Dossier> getDossiersClosed(String nom, int page, int size) {
		return dosRep.findAllClosed(nom, PageRequest.of(page, size));
	}

	
	
	@Override
	public Page<Dossier> getDossiersNotClosed(String nom, int page, int size) {
		return dosRep.findAllNotClosed(nom, PageRequest.of(page, size));
	}

	@Override
	public Page<Dossier> getDossiersNotClosedByDateCreation(String nom, Date dateCreation, int page, int size) {
		return dosRep.findAllNotClosedByDateCreation(nom, dateCreation, PageRequest.of(page, size));
	}

	@Override
	public List<Dossier> getDossiers() {
		return dosRep.findAll();
	}
 
}
