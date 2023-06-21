package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UserDto {
    private Long id;

    @NotEmpty(message = "Please enter valid name.")
    private String name;

    @NotEmpty(message = "Please enter valid email.")
    @Email
    private String email;

    @NotEmpty(message = "Please enter valid password.")
    private String password;

	public UserDto() {
		super();
	}

	public UserDto(Long id, @NotEmpty(message = "Please enter valid name.") String name,
			@NotEmpty(message = "Please enter valid email.") @Email String email,
			@NotEmpty(message = "Please enter valid password.") String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public UserDto(@NotEmpty(message = "Please enter valid name.") String name,
			@NotEmpty(message = "Please enter valid email.") @Email String email,
			@NotEmpty(message = "Please enter valid password.") String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public UserDto(@NotEmpty(message = "Please enter valid email.") @Email String email,
			@NotEmpty(message = "Please enter valid password.") String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
    
}
