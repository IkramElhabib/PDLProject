package com.example.demo.security;




import static org.springframework.security.config.Customizer.withDefaults;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import jakarta.servlet.Filter;


@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	  static HashMap<String, String> pages = new HashMap<String, String>() {{
	        put("SHOW_DASHBOARD", "/");
	        put("SHOW_STATISTIQUES", "/statistiques");
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
	                "/familles/save", "/familles/delete", "/tva/dave", "/tva/delete"},
	                new String[]{"UPDATE_PRODUITS"});
	    }};
	    
	    @Bean
	    public UserDetailsService userDetailsService(DataSource dataSource) {
	        return new JdbcUserDetailsManager(dataSource);
	    }
	    
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
	    @Configuration
	    public static class ApiSecurityConfig extends SecurityFilterAutoConfiguration {

	    	public void configure(HttpSecurity http) throws Exception {
	            http.authorizeHttpRequests(configurer -> configurer.anyRequest().authenticated())
	                    .formLogin(Customizer.withDefaults());
	        }
	    }
	    
	    @Configuration
	    public static class FormLoginSecurityConfig extends SecurityFilterAutoConfiguration {

	        
	        public void configure(HttpSecurity http) throws Exception {
	            http.authorizeHttpRequests(configurer -> configurer
	                    .requestMatchers("/login", "/resources/**", "/static/**").permitAll()
	                    .requestMatchers("/users/**", "/roles/**").hasRole("ADMIN")
	                    .requestMatchers("/masociete").authenticated()
	            );

	            http.formLogin(Customizer.withDefaults())
	                    .exceptionHandling(configurer -> configurer.accessDeniedPage("/403"));
	        }
	    }
	    
	    @Configuration
	    public static class PageSecurityConfig extends SecurityFilterAutoConfiguration {

	        
	        public void configure(HttpSecurity http) throws Exception {
	            pages.forEach((k, v) -> {
	                try {
	                    http.authorizeHttpRequests(configurer -> configurer
	                            .requestMatchers(v).hasAnyRole(k, "ADMIN")
	                    );
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            });

	            routes.forEach((k, v) -> {
	                try {
	                    http.authorizeHttpRequests(configurer -> configurer
	                            .requestMatchers(k).hasAnyRole(v[0], "ADMIN")
	                    );
	                } catch (Exception e)
	                {e.printStackTrace();}
	            
	        });

	        http.authorizeHttpRequests().anyRequest().denyAll();
	    }

	 	
}


}
