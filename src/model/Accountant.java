package model;

import java.util.ArrayList;

import exception.AlreadyExistException;
import exception.NotOwnedException;
import exception.NotRemovableException;

public class Accountant {

	//Attributes
	private String name;
	private ArrayList<Category> categories;
	
	//Constructor
	public Accountant(String name) {
		this.name = name;
		this.categories = new ArrayList<Category>();
	}
	
	//Methods
		//Add
	public void addCategory(Category category) throws AlreadyExistException{
		if(searchCategory(category.getName()) == null){
			categories.add(category);
		}
		else{
			throw new AlreadyExistException();
		}
	}
	
		//Remove
	public void removeCategory(Category category) throws NotRemovableException, NotOwnedException {
		int index = categories.indexOf(category);
		
		if(index != -1){
			if(categories.get(index).getPpes().size() == 0) {
				categories.remove(index);
			}
			else {
				throw new NotRemovableException();
			}
		}
		else {
			throw new NotOwnedException();
		}
	}
	
		//Search
	public Category searchCategory(String name) {
		Category category = null;
		
		boolean run = true;
		for(int i = 0; (i < categories.size()) && run; i++){
			if(categories.get(i).getName().equalsIgnoreCase(name)) {
				category = categories.get(i);
				run = false;
			}
		}
		
		return category;
	}
	
	public ArrayList<PPE> searchPPEByName(String aproxName) {
		ArrayList<PPE> ppes = new ArrayList<PPE>();
		for(Category category : categories){
			ppes.addAll(category.getPpes());
		}
		
		ArrayList<PPE> ppesName = new ArrayList<PPE>();
		for(PPE ppe : ppes){
			if(aproxName.equalsIgnoreCase(ppe.getName().substring(0,aproxName.length()))) {
				ppesName.add(ppe);
			}
		}
		
		return ppesName;
	}
	
	public ArrayList<PPE> searchPPEByEntrusted(String aproxEntrusted) {
		ArrayList<PPE> ppes = new ArrayList<PPE>();
		for(Category category : categories){
			ppes.addAll(category.getPpes());
		}
		
		ArrayList<PPE> ppesEntrusted = new ArrayList<PPE>();
		for(PPE ppe : ppes){
			if(aproxEntrusted.equalsIgnoreCase(ppe.getEntrusted().substring(0,aproxEntrusted.length()))) {
				ppesEntrusted.add(ppe);
			}
		}
		
		return ppesEntrusted;
	}
	
		//Modify
	public void editCategory(Category category, String name) throws AlreadyExistException, NotOwnedException {
		int index = categories.indexOf(category);
		
		if(index != -1){
			if(category.getName().equalsIgnoreCase(name) || (searchCategory(name) == null)){
				categories.get(index).setName(name);
			}
			else{
				throw new AlreadyExistException();
			}
			
		}
		else {
			throw new NotOwnedException();
		}
	}
	
		//Get
	public String getName() {
		return name;
	}
	
	public ArrayList<Category> getCategories(){
		return categories;
	}
		
}
