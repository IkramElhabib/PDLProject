package com.example.demo.security;


import java.io.IOException;

import com.example.demo.entities.Dossier;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = {	"/commandes","/factures",
		"/nouveaucommande","/nouveaufacture",
		"/editcommande","/editfacture",
		"/updatecommande","/updatefacture",
		"/deletecommande","/deletefacture",
		"/commande","/facture"}) 
public class DossierFilter implements Filter{

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
	        HttpServletResponse response = (HttpServletResponse ) res;
	             
	            Dossier dos = (Dossier) request.getSession().getAttribute("dossier"); 
	            if(dos==null) 
	            	response.sendRedirect(request.getContextPath()+"/dossiers?err=1"); 
	            else chain.doFilter(req, res);
		
	}
	
	 @Override public void init(FilterConfig arg0) throws ServletException {}
	   @Override public void destroy() {}

}
