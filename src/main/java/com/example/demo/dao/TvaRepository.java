package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Tva;

@Repository
public interface TvaRepository extends JpaRepository<Tva, Long>  
{
}
