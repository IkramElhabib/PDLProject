package com.example.demo.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>  
{
	@Query("select u from User u where u.username = :x")
	public User findByUserName( @Param("x")String username );

	@Query("select u from User u where u.username like :x order by username")
	public Page<User> findAllByMotCle(@Param("x")String motCle, Pageable pageable);  
}
