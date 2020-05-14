package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import exception.AlreadyExistException;
import exception.NotOwnedException;
import exception.NotRemovableException;

public class Accountant implements Serializable{

	//Constants
	private static final long serialVersionUID = 1L;
	public final static String DATA_PATH = "dat/ppe.app";
	
	//Attributes
	private String name;
	private ArrayList<Category> categories;
	
	//Constructor
	public Accountant(String name) {
		this.name = name;
		this.categories = new ArrayList<Category>();
		load();
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
			if(aproxName.length() <= ppe.getName().length()) {
				if(aproxName.equalsIgnoreCase(ppe.getName().substring(0,aproxName.length()))) {
					ppesName.add(ppe);
				}
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
			if(aproxEntrusted.length() <= ppe.getEntrusted().length()) {
				if(aproxEntrusted.equalsIgnoreCase(ppe.getEntrusted().substring(0,aproxEntrusted.length()))) {
					ppesEntrusted.add(ppe);
				}
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
	
		//Load
	public void load() {
		try{
			FileInputStream file=new FileInputStream(DATA_PATH);
			ObjectInputStream creator=new ObjectInputStream(file);
			Accountant me = (Accountant)creator.readObject();
			this.name = me.getName();
			this.categories = me.getCategories();
			creator.close();
		}
		catch (IOException e) {save();} 
		catch (ClassNotFoundException e) {}
	}
	
		//Save
	public void save() {
		try {
			FileOutputStream file=new FileOutputStream(DATA_PATH);
			ObjectOutputStream creator=new ObjectOutputStream(file);
			creator.writeObject(this);
			creator.close();
		}
		catch (IOException e) {}
	}
	
		//Set
	public void setName(String name) {
		this.name = name;
	}
	
		//Get
	public String getName() {
		return name;
	}
	
	public ArrayList<Category> getCategories(){
		return categories;
	}
		
}
