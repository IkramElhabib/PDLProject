package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	 @GetMapping("/logintest")
	    public String loginForm() {
	        return "login";
	    }
	 @GetMapping("/index")
	    public String indexTest() {
	        return "index";
	    }
}
