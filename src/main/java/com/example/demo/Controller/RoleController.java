package com.example.demo.Controller;

import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dao.RoleRepository;
import com.example.demo.entities.Role;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/roles")
public class RoleController {
    
    private final RoleRepository roleRepository;
    
    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    
    @GetMapping("/add")
    public String showAddRoleForm(Model model) {
        Role roless = new Role();
        model.addAttribute("roless", roless);
        long randomId = generateRandomId();
        roless.setId(randomId);
        
        return "ajoutroles";
    }
    
    @PostMapping("/add")
    public String addRole(@ModelAttribute("roless") @Valid Role role, BindingResult bindingResult) {
    	if (bindingResult.hasErrors()) {
    	    for (FieldError error : bindingResult.getFieldErrors()) {
    	        System.out.println(error.getField() + ": " + error.getDefaultMessage());
    	    }
    	    return "ajoutroles";
    	}
        
               
        roleRepository.save(role);
        
        return "redirect:/roles";
    }    
    private long generateRandomId() {
        Random random = new Random();
        return random.nextLong() & Long.MAX_VALUE;
    }}
