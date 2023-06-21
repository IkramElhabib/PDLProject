package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dao.UserRepository;
import com.example.demo.dto.UserDto;
import com.example.demo.entities.User;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@Controller
public class LoginController {
	
	 @Autowired
	    private UserService userService;
	 
	 @Autowired
	 	private UserRepository userRepository;
	
	 @GetMapping("/logintest")
	    public String loginForm() {
	        return "login";
	    }
	 @GetMapping("/indextest")
	    public String indexTest() {
	        return "index";
	    }
	 @GetMapping("/registration")
	    public String registrationForm(Model model) {
	        UserDto user = new UserDto();
	        model.addAttribute("user", user);
	        return "registration";
	    }
	 
	 @PostMapping("/registration")
	    public String registration( @Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
	        User existingUser = userService.findUserByEmail(user.getUsername());

	        if (existingUser != null)
	            result.rejectValue("email", null,
	                    "User already registered !!!");

	        if (result.hasErrors()) {
	            model.addAttribute("user", user);
	            return "/registration";
	        }

	        userRepository.save(user);
			
			
	        return "redirect:/login";
	    }
}
