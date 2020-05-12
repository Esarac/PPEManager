package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import exception.NotOwnedException;
import util.Tuple;

public class PPE implements Serializable{

	//Constants
	private static final long serialVersionUID = 1L;
	public enum State{
		OWNED, DEPRECIATION, SOLD, DAMAGED, IMPAIRMENT;
	}
	
	//Attributes
	private String name;
	private String entrusted;
	private String description;
	
	public int units;
	private State state;
	private GregorianCalendar date;
	
	private double value;
	private int lifespan;
	private ArrayList<Tuple<Double, GregorianCalendar>> deteriorations;
	private ArrayList<Tuple<Double, GregorianCalendar>> valorizations;
	
	//Constructor
	public PPE(String name, int units,double value, int lifespan, GregorianCalendar date, String entrusted, String description) {
		this.name = name;
		this.units = units;
		this.value = value;
		this.lifespan = lifespan;
		this.date = date;
		this.entrusted = entrusted;
		this.description = description;
		
		this.state = State.OWNED;
		this.deteriorations = new ArrayList<Tuple<Double, GregorianCalendar>>();
		this.valorizations = new ArrayList<Tuple<Double, GregorianCalendar>>();
	}
	
	//Methods
		//Add
	public void addDeterioration(double value, GregorianCalendar date) throws NotOwnedException{
		if(state.equals(State.OWNED)){
			
			deteriorations.add(new Tuple<Double, GregorianCalendar>(value, date));
			if(isLost()) {
				this.state = State.DAMAGED;
			}
			
		}
		else{
			throw new NotOwnedException();
		}
	}
	
	public void addDeterioration(double value) throws NotOwnedException{
		addDeterioration(value, new GregorianCalendar());
	}
	
	public void addValorizations(double value, GregorianCalendar date) throws NotOwnedException {
		if(state.equals(State.OWNED)){
			
			valorizations.add(new Tuple<Double, GregorianCalendar>(value, date));
			if((value < 0) && (isLost())) {
				this.state = State.IMPAIRMENT;
			}
			
		}
		else{
			throw new NotOwnedException();
		}
	}
	
	public void addValorizations(double value) throws NotOwnedException {
		addValorizations(value, new GregorianCalendar());
	}
	
		//Remove
	public void sell() throws NotOwnedException {
		if(state.equals(State.OWNED)){
			this.state = State.SOLD;
		}
		else{
			throw new NotOwnedException();
		}
	}
	
	public void depreciated(){
		if( (state.equals(State.OWNED)) && (isLost()) ){
			this.state = State.DEPRECIATION;
		}
	}
	
		//Verifier
	public boolean isLost(GregorianCalendar actualDate) {
		return calculateNetValue(actualDate) <= 0;
	}
	
	public boolean isLost() {
		return calculateNetValue() <= 0;
	}
	
		//Unit Value
	public double calculateUnitValue(GregorianCalendar actualDate){
		return calculateNetValue(actualDate) / units;
	}
	
	public double calculateUnitValue(){
		return value / units;
	}
		//Accumulated Depreciation
	public double calculateAccumulatedDepreciation(GregorianCalendar actualDate) {
		LocalDate date = this.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate lActualDate = actualDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		double accumulatedDepreciation = (value / lifespan) * ChronoUnit.MONTHS.between(date, lActualDate);
		
		return accumulatedDepreciation*-1;
	}
	
	public double calculateAccumulatedDepreciation() {
		return calculateAccumulatedDepreciation(new GregorianCalendar());
	}
	
		//Accumulated Deterioration
	public double calculateAccumulatedDeterioration(GregorianCalendar actualDate){
		double impaired = 0;
		
		for(int i = 0; i < deteriorations.size(); i++){
			if(deteriorations.get(i).getVal2().compareTo(actualDate) <= 0){
				impaired += deteriorations.get(i).getVal1();
			}
		}
		
		return impaired;
	}
	
	public double calculateAccumulatedDeterioration(){
		return calculateAccumulatedDeterioration(new GregorianCalendar());
	}
	
		//Accumulated Valorization
	public double calculateAccumulatedValorization(GregorianCalendar actualDate){
		double valorization = 0;
		
		for(int i = 0; i < valorizations.size(); i++){
			if(valorizations.get(i).getVal2().compareTo(actualDate) <= 0){
				valorization += valorizations.get(i).getVal1();
			}
		}
		
		return valorization;
	}
	
	public double calculateAccumulatedValorization(){
		return calculateAccumulatedValorization(new GregorianCalendar());
	}
	
		//Net Value
	public double calculateNetValue(GregorianCalendar actualDate) {
		return value + calculateAccumulatedDepreciation(actualDate) + calculateAccumulatedDeterioration(actualDate) + calculateAccumulatedValorization(actualDate);
	}
	
	public double calculateNetValue() {
		return value + calculateAccumulatedDepreciation() + calculateAccumulatedDeterioration() + calculateAccumulatedValorization();
	}
	
		//Report
	public String[][] showDepreciationReport(){
		String[][] table = new String[lifespan+1][9];
		
		for(int i = 0; i < table.length; i++){
			GregorianCalendar actualDate = (GregorianCalendar)date.clone();
			actualDate.add(GregorianCalendar.MONTH, i);
			
			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
			
			table[i][0] = this.name;//Name
			table[i][1] = f.format(actualDate.getTime());//Date
			table[i][2] = this.value + "";//Historical Value
			table[i][3] = calculateUnitValue() + "";//Historical Unit Value
			
			table[i][4] = calculateAccumulatedDepreciation(actualDate) + "";//Accumulated Depreciation
			table[i][5] = calculateAccumulatedDeterioration(actualDate) + "";//Accumulated Deterioration
			table[i][6] = calculateAccumulatedValorization(actualDate) + "";//Accumulated Valorization
			table[i][7] = calculateNetValue(actualDate) + "";//Net Value
			table[i][8] = calculateUnitValue(actualDate) + "";//Net Unit Value
		}
		
		return table;
	}
	
		//Compare
	public int compareName(PPE ppe) {
		return name.compareToIgnoreCase(ppe.getName());
	}
	
	
		//Set
	public void setName(String name) {
		this.name = name;
	}
	
	public void setEntrusted(String entrusted) {
		this.entrusted = entrusted;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
		//Get
	public String getName() {
		return name;
	}
	
	public String getEntrusted() {
		return entrusted;
	}
	
	public String getDescription() {
		return description;
	}

	public int getUnits() {
		return units;
	}

	public State getState() {
		return state;
	}

	public GregorianCalendar getDate() {
		return date;
	}

	public double getValue() {
		return value;
	}

	public int getLifespan() {
		return lifespan;
	}

	public ArrayList<Tuple<Double, GregorianCalendar>> getDeteriorations() {
		return deteriorations;
	}

	public ArrayList<Tuple<Double, GregorianCalendar>> getValorizations() {
		return valorizations;
	}
	
	public String toString() {
		return name;
	}
	
}
