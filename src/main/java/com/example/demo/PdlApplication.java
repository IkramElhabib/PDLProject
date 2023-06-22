package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.example.demo.security.SecurityConfig;

@SpringBootApplication
@Import(SecurityConfig.class) 
public class PdlApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdlApplication.class, args);
	}

}
