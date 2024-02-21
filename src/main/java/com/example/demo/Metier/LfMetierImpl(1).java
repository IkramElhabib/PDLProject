package com.example.demo.Metier;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.IMetier.ILfMetier;
import com.example.demo.dao.LfRepository;
import com.example.demo.entities.LigneFacture;

@Service
public class LfMetierImpl implements ILfMetier
{ 
	@Autowired
	private LfRepository lnfRep;

	@Override
	public LigneFacture getLigneFacture(Long id) {
		// TODO Auto-generated method stub
		return lnfRep.getOne(id);
	}

	@Override
	public LigneFacture saveLigneFacture(LigneFacture lc) {
		// TODO Auto-generated method stub
		return lnfRep.save(lc);
	}

	@Override
	public boolean deleteLigneFacture(Long id) {
		// TODO Auto-generated method stub
		LigneFacture lc = lnfRep.getOne(id);
		if(lc==null) return false;
		lnfRep.delete(lc);
		return true;
	}

	@Override
	public Page<LigneFacture> getLignesFacture(Date d1, Date d2) {
		return lnfRep.findAllBetween(d1,d2,PageRequest.of(0, 10));
	}
 
 
 
}

