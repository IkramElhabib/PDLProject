package com.example.demo.dao;

import java.util.Optional;
import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Fournisseur;
import com.example.demo.entities.Produit;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, String>  
{
	//@Query("select frs from Fournisseur frs where frs.code = :x")
	//public Fournisseur findByCode( @Param("x")String code );
 
	Fournisseur  findByCode(String code);
	@Query("select p from Fournisseur p where ( p.code like :x or p.nom like :x or p.raisonSociale like :x or p.capital like :x or p.adresse like :x or p.email like :x or p.tel like :x or p.fax like :x ) and  p.code!='CODE_0'")
	public Page<Fournisseur> findAllByMotCle( @Param("x")String mc, Pageable pageable );
	
	List<Fournisseur> findAll();
	
	@Transactional
	@Modifying
	@Query("UPDATE Fournisseur f SET f.nom = :nom, f.email = :email, f.raisonSociale = :raisonSociale, f.adresse = :adresse, f.capital = :capital, f.tel = :tel, f.fax = :fax WHERE f.code = :code")
	void updateFournisseur(@Param("code") String code, @Param("nom") String nom, @Param("email") String email, @Param("raisonSociale") String raisonSociale, @Param("adresse") String adresse, @Param("capital") Double capital, @Param("tel") String tel, @Param("fax") String fax);

	
    void delete(Fournisseur fournisseur);
}