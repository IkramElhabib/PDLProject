package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Produit;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long>
{
	@Query("select p from Produit p where p.ref=:x")
	public Produit getById( @Param("x")String ref );
	

}