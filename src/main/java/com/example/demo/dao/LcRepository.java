package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.LigneCommande;

@Repository
public interface LcRepository extends JpaRepository<LigneCommande, Long>
{
	@Query("select p from LigneCommande p where p.produit.ref=:x and p.commande.numero=:num")
	public LigneCommande findLcByProduit( @Param("num")Long numCommande, @Param("x")String ref );
}
