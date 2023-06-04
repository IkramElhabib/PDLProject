package com.example.demo.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Dossier;
import com.example.demo.entities.Fournisseur;

@Repository
public interface DossierRepository extends JpaRepository<Dossier, Long>  
{  
	@Query("select p from Dossier p where p.nom like :n order by p.dateCreation desc")
	public Page<Dossier> findAll(
			@Param("n")String nom, Pageable pageable); 

	
	@Query("select p from Dossier p where p.nom like :n and (p.dateFermeture=:df) order by p.dateCreation desc")
	public Page<Dossier> findAllByDateFermeture(
			@Param("n")String nom, @Param("df")Date dateFermeture, Pageable pageable); 

	
	@Query("select p from Dossier p where p.nom like :n and p.dateCreation=:do order by p.dateCreation desc")
	public Page<Dossier> findAllByDateCreation(
			@Param("n")String nom, @Param("do")Date dateCreation, Pageable pageable); 
 
	
	@Query("select p from Dossier p where p.nom like :n and p.dateCreation=:do and p.dateFermeture=:df order by p.dateCreation desc")
	public Page<Dossier> findAllByDateCreationFermeture(
			@Param("n")String nom, @Param("do")Date dateCreation,@Param("df")Date dateFermeture, Pageable pageable);
	
	
	@Query("select p from Dossier p where p.nom like :n and p.dateCreation=:do and p.dateFermeture!=null order by p.dateCreation desc")
	public Page<Dossier> findAllClosedByDateCreation(
			@Param("n")String nom, @Param("do")Date dateCreation, Pageable pageable);
	
	
	@Query("select p from Dossier p where p.nom like :n and p.dateFermeture!=null order by p.dateCreation desc")
	public Page<Dossier> findAllClosed(
			@Param("n")String nom, Pageable pageable);

	
	
	
	
	@Query("select p from Dossier p where p.nom like :n and p.dateFermeture=null order by p.dateCreation desc")
	public Page<Dossier> findAllNotClosed(
			@Param("n")String nom, Pageable pageable);

	@Query("select p from Dossier p where p.nom like :n and p.dateFermeture=null and p.dateCreation=:do  order by p.dateCreation desc")
	public Page<Dossier> findAllNotClosedByDateCreation(
			@Param("n")String nom,@Param("do")Date dateCreation, Pageable pageable);
	/* ------------------------------- */
	
	List<Dossier> findAll();
	Dossier save(Dossier dossier);
	
	/*@Transactional
	@Modifying
	@Query("UPDATE Fournisseur f SET f.nom = :nom, f.email = :email, f.raisonSociale = :raisonSociale, f.adresse = :adresse, f.capital = :capital, f.tel = :tel, f.fax = :fax WHERE f.code = :code")
	void updateDossier(@Param("code") String code, @Param("nom") String nom, @Param("email") String email, @Param("raisonSociale") String raisonSociale, @Param("adresse") String adresse, @Param("capital") Double capital, @Param("tel") String tel, @Param("fax") String fax);

	
    void delete(Fournisseur fournisseur);*/
	Dossier findByNumero(Long numero);
	
    @Modifying
    @Query("UPDATE Dossier d SET d.dateFermeture = :dateFermeture WHERE d.numero = :numero")
    void fermerDossier(@Param("numero") Long numero, @Param("dateFermeture") LocalDate dateFermeture);
}
