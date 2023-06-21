package com.example.demo.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserRepository;
import com.example.demo.dto.UserDto;
import com.example.demo.entities.User;


@Service
public class UserServiceImpl implements UserService{
	
	 @Autowired
	    private UserRepository userRepository;

		

		@Override
		public User findUserByEmail(String email) {
			// TODO Auto-generated method stub
	        return userRepository.findByUsername(email);
		}

		
}
