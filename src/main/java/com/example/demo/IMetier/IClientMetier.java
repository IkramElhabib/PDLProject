package com.example.demo.IMetier;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.entities.Client;

public interface IClientMetier 
{ 
	public List<Client> getClients(); 
	public Page<Client> getClients(int page, int size); 
	public Page<Client> getClientsByMotCle(String mc,int page, int size); 
	public Client getClient( String code );   
	public Client saveClient(Client frs);
	public boolean deleteClient(String code); 
}
