package com.example.demo.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Client;
import com.example.demo.entities.Fournisseur;
import com.example.demo.entities.Produit;

@Repository
public interface ClientRepository extends JpaRepository<Client, String>  
{
	//@Query("select c from Client c where c.code = :x")
	 Client findByCode(String code); 
	
	@Query("select p from Client p where p.code like :x or p.nom like :x or p.prenom like :x or p.age like :x or p.adresse like :x or p.email like :x or p.tel like :x ")
	public Page<Client> findAllByMotCle( @Param("x")String mc, Pageable pageable );
	
	Client save(Client client);
	
	void delete(Client client);
    List<Client> findAll() ;
    
    @Transactional
	@Modifying
	@Query("UPDATE Client c SET c.nom = :nom, c.email = :email, c.prenom = :prenom, c.age = :age, c.adresse = :adresse, c.tel = :tel WHERE c.code = :code")
	void updateClient(@Param("code") String code, @Param("nom") String nom, @Param("email") String email, @Param("prenom") String prenom, @Param("adresse") String adresse, @Param("age") int age, @Param("tel") String tel);


}
