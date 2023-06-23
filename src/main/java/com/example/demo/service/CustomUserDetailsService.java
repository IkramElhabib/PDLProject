package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserRepository;
import com.example.demo.entities.User;


@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
    private UserRepository userRepository;
	    @Override
	    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
	        User user = userRepository.findByUsername(usernameOrEmail);

	        if (user != null) {
	            return new org.springframework.security.core.userdetails.User(
	                user.getUsername(),
	                user.getPassword(),
	                new ArrayList<>()
	            );
	        } else {
	            throw new UsernameNotFoundException("Invalid email or password");
	        }
	    }

}