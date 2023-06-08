package com.example.demo.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Facture;

	@Repository
	public interface FactureRepository extends JpaRepository<Facture, Long>  
	{
		@Query("select p from Facture p where p.numero=:num")
		public Facture find(@Param("num")Long numero);
	 
		
		@Query("select p from Facture p where p.fournisseur=null and p.dossier.numero=:num")
		public Page<Facture> findAllFacturesClients(@Param("num")Long numeroDossier, Pageable pageable);
	 
		@Query("select p from Facture p where p.fournisseur=null and p.dateFacture=:x and p.dossier.numero=:num")
		public Page<Facture> findAllFacturesClients(@Param("num")Long numeroDossier,@Param("x")Date date,Pageable pageable);
		  
		
		@Query("select p from Facture p where p.client.code=:x and p.dossier.numero=:num")
		public Page<Facture> findAllFacturesOfClient(@Param("num")Long numeroDossier,@Param("x")String code, Pageable pageable);
	 
		@Query("select p from Facture p where p.client.code=:c and p.dateFacture=:d and p.dossier.numero=:num")
		public Page<Facture> findAllFacturesOfClient(@Param("num")Long numeroDossier,@Param("c")String code,@Param("d")Date date, Pageable pageable);
	 
		

		@Query("select p from Facture p where p.client=null and p.dossier.numero=:num")
		public Page<Facture> findAllFacturesFournisseurs(@Param("num")Long numeroDossier, Pageable pageable);

		@Query("select p from Facture p where p.client=null and p.dateFacture=:x and p.dossier.numero=:num")
		public Page<Facture> findAllFacturesFournisseurs(@Param("num")Long numeroDossier,@Param("x")Date date, Pageable pageable);
	 
		@Query("select p from Facture p where p.fournisseur.code=:x and p.dossier.numero=:num")
		public Page<Facture> findAllFacturesOfFournisseur(@Param("num")Long numeroDossier,@Param("x")String code,  Pageable pageable);

		@Query("select p from Facture p where p.fournisseur.code=:x and p.dateFacture=:d and p.dossier.numero=:num")
		public Page<Facture> findAllFacturesOfFournisseur(@Param("num")Long numeroDossier,@Param("x")String code,@Param("d")Date date,  Pageable pageable);
	  
	 
		
		@Query("select count(p) from Facture p where p.dossier.numero=:num")
		public Integer count(@Param("num")Long numeroDossier);
		 
		@Query("select count(l) from LigneFacture l where l.facture.client=null and l.facture.dossier.numero=:num")
		public Integer countAchatsProduits(@Param("num")Long numeroDossier);
		
		@Query("select count(l) from LigneFacture l where  l.facture.fournisseur=null and l.facture.dossier.numero=:num")
		public Integer countVentesProduits(@Param("num")Long numeroDossier);
		 
		@Query("select coalesce(sum(p.ttc),0) from Facture p where p.client=null and p.dossier.numero=:num")
		public Double prixAchatsProduits(@Param("num")Long numeroDossier);

		@Query("select coalesce(sum(p.ttc),0) from Facture p where p.fournisseur=null and p.dossier.numero=:num")
		public Double prixVentesProduits(@Param("num")Long numeroDossier);
		
		Facture findByNumero(Long numero);
		List<Facture> findAll();
		Facture save(Facture facture);
		}
