package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import exception.AlreadyExistException;
import exception.NotOwnedException;

public class Category implements Serializable{

	//Constants
	private static final long serialVersionUID = 1L;
	
	//Attributes
	private String name;
	private ArrayList<PPE> ppes;
	
	//Constructor
	public Category(String name) {
		this.name = name;
		this.ppes = new ArrayList<PPE>();
	}
	
	//Methods
		//Add
	public void addPPE(PPE ppe) throws AlreadyExistException{
		if(searchPPE(ppe.getName()) == null){
			ppes.add(ppe);
		}
		else{
			throw new AlreadyExistException();
		}
	}
	
		//Search
	public PPE searchPPE(String name){
		PPE ppe = null;
		
		boolean run = true;
		for(int i = 0; (i < ppes.size()) && run; i++){
			if(ppes.get(i).getName().equalsIgnoreCase(name)) {
				ppe = ppes.get(i);
				run = false;
			}
		}
		
		return ppe;
	}
	
		//Modify
	public void editPPE(PPE ppe, String name, String entrusted, String description) throws NotOwnedException, AlreadyExistException {
		int index = ppes.indexOf(ppe);
		
		if(index != -1){
			if(ppe.getName().equalsIgnoreCase(name) || (searchPPE(name) == null)){
				if(name != null)
					ppes.get(index).setName(name);
				if(entrusted != null)
					ppes.get(index).setEntrusted(entrusted);
				if(description != null)
					ppes.get(index).setDescription(description);
			}
			else{
				throw new AlreadyExistException();
			}
			
		}
		else {
			throw new NotOwnedException();
		}
	}
	
		//Sort
	public void sortPPEByName(){
		sortPPEByName(0, (ppes.size() - 1));
	}
	
	private void sortPPEByName(int low, int high) {
		
		if(low < high) {
			PPE pivot = ppes.get(high);
			int i = low - 1;
			
			for(int j = low; j < high; j++) {
				if(ppes.get(j).compareName(pivot) < 0){
					
					i++;
					Collections.swap(ppes, i, j);
					
				}
			}
			
			Collections.swap(ppes, i+1, high);
			
			int pi = i + 1;
			
			sortPPEByName(low, pi -1);
			sortPPEByName(pi + 1, high);
		}
		
	}
	
		//Set
	public void setName(String name) {
		this.name = name;
	}
	
		//Get
	public String getName() {
		return name;
	}
	
	public ArrayList<PPE> getPpes(){
		return ppes;
	}
	
	public String toString() {
		return name;
	}
	
}
