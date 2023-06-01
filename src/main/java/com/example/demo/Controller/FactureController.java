package com.example.demo.Controller;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.example.demo.IMetier.IClientMetier;
import com.example.demo.IMetier.ICommandeMetier;
import com.example.demo.IMetier.IFactureMetier;
import com.example.demo.IMetier.IFournisseurMetier;
import com.example.demo.IMetier.ILcMetier;
import com.example.demo.IMetier.IProduitMetier;
import com.itextpdf.html2pdf.HtmlConverter;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;


@Controller
public class FactureController {
	
	@Autowired private IFactureMetier metierFacture;  
	@Autowired private ICommandeMetier metierCommande;  
	
	@Autowired private IProduitMetier metierProduit;
	@Autowired private IFournisseurMetier metierFournisseur;
	@Autowired private IClientMetier metierClient; 
	@Autowired private ILcMetier metierLigneFacture;
	
	
	
	@Autowired private HttpSession session;
	
	 @Autowired
	    private ServletContext servletContext;
	 
	 @RequestMapping(value= {"/factures/preview"})
		public String previewFacture(Model model,@RequestParam(name="numero",defaultValue="0")Long num)
		{
			try {
				com.example.demo.entities.Facture fct = metierFacture.getFacture(num);
		 		if(fct==null) return "redirect:/factures";
		 		 System.err.println("path : "+servletContext.getContextPath());
		 		model.addAttribute("url", servletContext.getContextPath() );
		 		model.addAttribute("facture",fct);
		 		model.addAttribute("nbrProduits",0);
		 		model.addAttribute("maSociete", metierFournisseur.getFournisseur("CODE_0"));
			}catch (Exception e) {}
			
			return "factureimpr";
		}
	 
	 @RequestMapping(value= {"/factures/print"})
		public ResponseEntity<InputStreamResource> index(Model model,@RequestParam(name="num",defaultValue="0")Long num,
			@RequestParam(name="dest",defaultValue="")String dest)
		{
			try {
				com.example.demo.entities.Facture fct = metierFacture.getFacture(num);
		 		if(fct==null) return null;
		 		
		    	ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		    	templateResolver.setSuffix(".html");
		    	templateResolver.setTemplateMode("HTML");
		    	 
		    	TemplateEngine templateEngine = new TemplateEngine();
		    	templateEngine.setTemplateResolver(templateResolver);
		    	 
		    	Context context = new Context();
		    	ClassPathResource rcss = new ClassPathResource("/static/resources");
		    	context.setVariable("url", rcss.getFile().getAbsolutePath());
		    	context.setVariable("facture", fct);
		    	context.setVariable("nbrProduits", 0);
		    	context.setVariable("maSociete", metierFournisseur.getFournisseur("CODE_0"));
	  
		    	ClassPathResource r = new ClassPathResource("/templates/factureimpr");
		    	String html = templateEngine.process(r.getPath(), context);
		 		 
		 		ByteArrayOutputStream out = new ByteArrayOutputStream(); 
		    	try { HtmlConverter.convertToPdf(html, out); } 
		    	catch (Exception e) { e.printStackTrace(); }
		    	  
		    	ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
		 				
		        HttpHeaders headers = new HttpHeaders();
		        headers.add("Content-Disposition", "inline; filename=Facture_"+fct.getNumero()+".pdf");

		        return ResponseEntity
		                .ok()
		                .headers(headers)
		                .contentType(MediaType.APPLICATION_PDF)
		                .body(new InputStreamResource(bis));
			} 
			catch (Exception e) { e.printStackTrace(); } 
			return null;
		}

}
