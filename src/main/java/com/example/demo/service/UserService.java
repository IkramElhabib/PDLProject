package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entities.User;


public interface UserService {
       User findUserByEmail(String email);
    
    
}