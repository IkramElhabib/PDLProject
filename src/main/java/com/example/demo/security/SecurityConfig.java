package com.example.demo.security;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import jakarta.servlet.Filter;


@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	@Autowired
	private DataSource dataSource;

	static HashMap<String, String> pages = new HashMap<String, String>() {{
		put("SHOW_DASHBOARD", "/");
		put("SHOW_DOSSIERS", "/dossiers");
		put("SHOW_COMMANDES", "/commandes");
		put("SHOW_FACTURES", "/factures");
		put("SHOW_PRODUCTS", "/produits");
		put("SHOW_CLIENTS", "/clients");
		put("SHOW_FOURNISSEURS", "/fournisseurs");
	}};
	
	static HashMap<String[], String[]> routes = new HashMap<String[], String[]>() {{
		put(new String[]{"/dossiers/add", "/dossiers/update", "/dossiers/delete"},
				new String[]{"UPDATE_DOSSIERS"});
		put(new String[]{"/commandes/nouveau", "/commandes/edit", "/commandes/add", "/commandes/update", "/commandes/delete"},
				new String[]{"UPDATE_COMMANDES"});
		put(new String[]{"/factures/nouveau", "/factures/edit", "/factures/add", "/factures/update", "/factures/delete"},
				new String[]{"UPDATE_FACTURES"});
		put(new String[]{"/factures/print", "/factures/preview"},
				new String[]{"ROLE_PRINT_FACTURES"});
		put(new String[]{"/fournisseurs/add", "/fournisseurs/update", "/fournisseurs/delete"},
				new String[]{"UPDATE_FOURNISSEURS"});
		put(new String[]{"/produits/add", "/produits/update", "/produits/delete",
				"/familles/save", "/familles/delete", "/tva/save", "/tva/delete"},
				new String[]{"UPDATE_PRODUITS"});
	}};
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select username as principal, password as credentials, active from user where username=?")
				.authoritiesByUsernameQuery("select username as principal, role from users_roles ur,role r where ur.username=? and ur.role_id=r.id")
				.rolePrefix("ROLE_")
				.passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		JdbcDaoImpl jdbcDaoImpl = new JdbcDaoImpl();
		jdbcDaoImpl.setDataSource(dataSource);
		return jdbcDaoImpl;
	}
	
	@Configuration
	@EnableWebSecurity
	public class SecurityFilter extends AbstractSecurityWebApplicationInitializer {
	}
	
	
	
	
	


}
