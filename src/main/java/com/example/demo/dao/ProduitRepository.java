package com.example.demo.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Produit;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, String>
{
	@Query("select p from Produit p where p.ref=:x")
	public Produit getById( @Param("x")String ref );
	
	Produit save(Produit produit);
	
    Optional<Produit> findByRef(String ref);
    
    @Transactional
    @Modifying
    @Query("UPDATE Produit p SET p.designation = :designation, p.prix = :prix, p.quantite = :quantite, p.quantiteAlert = :quantiteAlert WHERE p.ref = :ref")
    void updateProduit(@Param("ref") String ref, @Param("designation") String titre, @Param("prix") double prix, @Param("quantite") int quantite, @Param("quantiteAlert") int quantiteAlert);
    
    void delete(Produit produit);

	

}
