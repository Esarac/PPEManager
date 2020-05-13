package controller;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;
import exception.AlreadyExistException;
import exception.NotOwnedException;
import exception.NotRemovableException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Accountant;
import model.Category;
import model.PPE;
import model.PPE.State;

public class ControlMenu implements Initializable {
	
	//Constants
	public final static String BACK_SYMBOL="«";
	public final static String ADD_SYMBOL="+";
	public final static String MINIMIZE_SYMBOL="-";
	public final static String EDIT_SYMBOL="...";
	public final static String DONE_SYMBOL="\u2714";
	public final static String MARKED_SYMBOL="!";
	public final static String TIMELINE_SYMBOL="\u23F3";
	public final static String SEARCH_SYMBOL="\u26B2";
	
	public final static String ICONS_PATH="med/icon/category/";
	public final static String DEFAULT_ICONS_PATH="med/icon/default/";
	
	//Attributes
	private Accountant accountant;
	private String[] identifiers;
	
	//Nodes
	@FXML private HBox header;
	@FXML private VBox pane;
	private VBox information;
	@FXML private ListView<HBox> list;
	private ContextMenu itemMenu;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		accountant=new Accountant("Empresa Farmaceutica Ossim");
		identifiers=new String[2];
	}
	
	public class PPETableView {

		private SimpleStringProperty name;
		private SimpleStringProperty date;
		private SimpleStringProperty historic;
		private SimpleStringProperty unitHistoric;
		private SimpleStringProperty depretation;
		private SimpleStringProperty damaged;
		private SimpleStringProperty valorization;
		private SimpleStringProperty net;
		private SimpleStringProperty unitNet;
		
		public PPETableView(String name, String date, String historic, String unitHistoric, String depretation, String damaged, String valorization, String net, String unitNet) {
			this.name = new SimpleStringProperty(name);
			this.date = new SimpleStringProperty(date);
			this.historic = new SimpleStringProperty(historic);
			this.unitHistoric = new SimpleStringProperty(unitHistoric);
			this.depretation = new SimpleStringProperty(depretation);
			this.damaged = new SimpleStringProperty(damaged);
			this.valorization = new SimpleStringProperty(valorization);
			this.net = new SimpleStringProperty(net);
			this.unitNet = new SimpleStringProperty(unitNet);
		}
		
		public String getValorization() {
			return valorization.get();
		}
		
		public void setValorization(String valorization) {
			this.valorization = new SimpleStringProperty(valorization);
		}
		
		public String getName() {
			return name.get();
		}

		public void setName(String name) {
			this.name = new SimpleStringProperty(name);
		}

		public String getDate() {
			return date.get();
		}

		public void setDate(String date) {
			this.date = new SimpleStringProperty(date);
		}

		public String getHistoric() {
			return historic.get();
		}

		public void setHistoric(String historic) {
			this.historic = new SimpleStringProperty(historic);
		}

		public String getUnitHistoric() {
			return unitHistoric.get();
		}

		public void setUnitHistoric(String unitHistoric) {
			this.unitHistoric = new SimpleStringProperty(unitHistoric);;
		}

		public String getDepretation() {
			return depretation.get();
		}

		public void setDepretation(String depretation) {
			this.depretation = new SimpleStringProperty(depretation);
		}

		public String getDamaged() {
			return damaged.get();
		}

		public void setDamaged(String damaged) {
			this.damaged = new SimpleStringProperty(damaged);
		}

		public String getNet() {
			return net.get();
		}

		public void setNet(String net) {
			this.net = new SimpleStringProperty(net);
		}

		public String getUnitNet() {
			return unitNet.get();
		}

		public void setUnitNet(String unitNet) {
			this.unitNet = new SimpleStringProperty(unitNet);
		}
	}
	
	//Generators
	public void generate() {
		
		accountant.save();
		
		header.getChildren().clear();
		list.getItems().clear();
		pane.getChildren().remove(information);
		pane.setPadding(new Insets(50, 50, 50, 50));
		if(itemMenu!=null){itemMenu.hide();}
		header.setAlignment(Pos.CENTER);
		
		if(identifiers[1]!=null){//PPE
			generatePPE();
		}
		else if(identifiers[0]!=null) {//Category
			generateCategory();
		}
		else{//Accountant
			generateAccountant();
		}
	}

	public void generateAccountant() {//Example
		
		//HEADER
		//~SEARCH
		Button search=new Button(SEARCH_SYMBOL);
		search.setOnMouseClicked(event->{
			TextField searchBar=new TextField();
			searchBar.setPromptText("buscar");
			header.getChildren().set(1, searchBar);
			
			search.setOnMouseClicked(kEvent->{
				generate();
			});
			searchBar.setOnKeyPressed(kEvent->{
        		if(kEvent.getCode().equals(KeyCode.ENTER)){
        			
        			list.getItems().clear();
        			pane.getChildren().remove(information);
        			if(itemMenu!=null){itemMenu.hide();}
        			header.setAlignment(Pos.CENTER);
        			
        			//List Name
        			ArrayList<PPE> name = accountant.searchPPEByName(searchBar.getText());
        			generateSearchList(name, "(Nombre)");
        			
        			//List Entrusted
        			ArrayList<PPE> entrusted = accountant.searchPPEByEntrusted(searchBar.getText());
        			generateSearchList(entrusted, "(Encargado)");
        			
        		}
        	});
		});
		//~...
		
		//~APP NAME
		Label accountantName=new Label(accountant.getName());
		accountantName.getStyleClass().add("title");
		//~...
		
		//~ADD
		Button add=new Button(ADD_SYMBOL);
		add.setOnMouseClicked(event->{
			
			TextField categoryName=onActionAddButton(add);
			categoryName.setOnKeyPressed(kEvent->{
				
        		if(kEvent.getCode().equals(KeyCode.ENTER)){
        			try {
						accountant.addCategory(new Category(categoryName.getText()));
					}
        			catch (AlreadyExistException e) {
						showAlert("Ya existe una categoria con ese nombre");
					}
        			generate();
        		}
        		
        	});
		});
		//~...
		header.getChildren().addAll(search,accountantName, add);
		//...
		
		//List
		ArrayList<Category> categories=accountant.getCategories();
		for(int i=0; i<categories.size(); i++){
			Category category=categories.get(i);
			HBox itemBox=generateItemBox(category.toString(), category.getName()+".png", "Category.png");
			
			//OnAction
			itemBox.setOnMouseClicked(event->{
				//Open
				if(event.getButton() == MouseButton.PRIMARY){
					this.identifiers[0]=category.getName();
					generate();
				}
				//...
				//Option Menu
				else if(event.getButton()==MouseButton.SECONDARY){
					generateItemMenu();
					
					//Delete
					MenuItem delete = new MenuItem("Borrar");
			        delete.setOnAction(dEvent->{
			        	try {
							accountant.removeCategory(category);
						}
			        	catch (NotRemovableException e) {
							showAlert("No se puede borrar, porque tiene PPEs");
						}
			        	catch (NotOwnedException e) {
			        		showAlert("No posees este item");
						}
			        	generate();
			        });
			        //...
			        
			        //Edit
			        MenuItem name = new MenuItem("Cambiar Name");
					name.setOnAction(eEvent->{
			        	Node nameNode=itemBox.getChildren().get(1);
			        	itemBox.getChildren().remove(1);
			        	
			        	//toTextField
			        	if(nameNode instanceof Label){
				        	TextField categoryName=new TextField(category.toString());
				        	itemBox.getChildren().add(1,categoryName);
				        	
				        	categoryName.setOnKeyPressed(kEvent->{
				        		
				        		if(kEvent.getCode().equals(KeyCode.ENTER)){
				        			try {
										accountant.editCategory(category, categoryName.getText());
									}
				        			catch (AlreadyExistException e) {
				        				showAlert("Ya existe una categoria con ese nombre");
									}
				        			catch (NotOwnedException e) {
				        				showAlert("No posees este item");
									}
				        			
									generate();
				        		}
				        		
				        	});
			        	}
			        	//...
			        	//toLabel
			        	else if(nameNode instanceof TextField){
				        	itemBox.getChildren().add(1, new Label(category.toString()));
			        	}
			        	//...
			        	
					});
			        //...
					
			        //Image
			        MenuItem image = new MenuItem("Cambiar Imagen");
			        image.setOnAction(eEvent->{
			        	//Choose File
			        	Stage stage = (Stage) pane.getScene().getWindow();
				        FileChooser fileChooser = new FileChooser();
						fileChooser.setTitle("Image Selector");
						fileChooser.getExtensionFilters().add(new ExtensionFilter("PNG", "*.png"));
						File imageFile=fileChooser.showOpenDialog(stage);
						//...
						//Delete
						File newImageFile=new File(ICONS_PATH+category.getName()+".png");
						newImageFile.delete();
						//...
						//Change
						if(imageFile!=null){
							imageFile.renameTo(newImageFile);
						}
						generate();
						//...
						
			        });
			        //...
					
			        itemMenu.getItems().addAll(delete, name, image);
			        itemMenu.show(itemBox, event.getScreenX(), event.getScreenY());
			        
				}
				//...
			});
			//...
			
			list.getItems().add(itemBox);
		}
		//...
		
	}
	
	public void generateCategory() {
		
		//HEADER
		//~Back
		Button back=new Button(BACK_SYMBOL);
		back.setOnMouseClicked(event->{
			identifiers[0]=null;
			generate();
		});
		//~...
		//~Console Name
		Label categoryName=new Label(identifiers[0].toString());
		categoryName.getStyleClass().add("title");
		//~...
		//~Add
		Button add=new Button(ADD_SYMBOL);
		add.setOnMouseClicked(event->{
			generatePPECreator();
		});
		//~...
		header.getChildren().addAll(back,categoryName,add);
		//...	
		
		Category category = accountant.searchCategory(identifiers[0]);
		
		//List
		ArrayList<PPE> ppes = category.getPpes();
		for(int i=0; i<ppes.size(); i++){
			PPE ppe = ppes.get(i);
			ppe.depreciated();
			HBox itemBox=generateItemBox(ppe.toString(), identifiers[0]+"/"+ppe.getName()+".png", "PPE.png");
			
			//State
			String state=ppe.getState().toString();
			itemBox.getChildren().add(new Label(state));
			//...
			
			//OnAction
			itemBox.setOnMouseClicked(event->{
				
				//Open
				if(event.getButton()==MouseButton.PRIMARY){
					this.identifiers[1]=ppe.getName();
					generate();
				}
				//...
				//Option Menu
				else if(event.getButton()==MouseButton.SECONDARY){
					
					generateItemMenu();
					//Sell
					MenuItem delete = new MenuItem("Vender");
			        delete.setOnAction(dEvent->{
			        	try {
							ppe.sell();
						}
			        	catch (NotOwnedException e) {
							showAlert("No posees este PPE");
						}
			        	generate();
			        });
					//...
			        
			        //Image
			        MenuItem image = new MenuItem("Cambiar Imagen");
			        image.setOnAction(eEvent->{
			        	//Choose File
			        	Stage stage = (Stage) pane.getScene().getWindow();
				        FileChooser fileChooser = new FileChooser();
						fileChooser.setTitle("Image Selector");
						fileChooser.getExtensionFilters().add(new ExtensionFilter("PNG", "*.png"));
						File imageFile=fileChooser.showOpenDialog(stage);
						//...
						//CreateDir
						new File(ICONS_PATH+identifiers[0]).mkdir();
						//...
						//Delete
						File newImageFile=new File(ICONS_PATH+identifiers[0]+"/"+ppe.getName()+".png");
						newImageFile.delete();
						//...
						//Change
						if(imageFile!=null){
							imageFile.renameTo(newImageFile);
						}
						generate();
						//...
						
			        });
			        //...
			        
			        itemMenu.getItems().addAll(delete, image);
			        itemMenu.show(itemBox, event.getScreenX(), event.getScreenY());
			        
				}
				//...
				
			});
			//...
			
			list.getItems().add(itemBox);
			
		}
		//...
		
	}
	
	public void generatePPE() {
		//HEADER
		//~Back
		Button back=new Button(BACK_SYMBOL);
		back.setOnMouseClicked(event->{
			identifiers[1]=null;
			generate();
		});
		//~...
		//~Game Name
		Label ppeName=new Label(identifiers[1].toString());
		ppeName.getStyleClass().add("title");
		//...
		//~Edit
		Button edit=new Button(EDIT_SYMBOL);
		edit.setOnMouseClicked(event->{
			generatePPEEditor();
		});
		//~...
		header.getChildren().addAll(back,ppeName,edit);
		//...
		
		PPE ppe = accountant.searchCategory(identifiers[0]).searchPPE(identifiers[1]);
		
		//PPE
		information=new VBox();
		information.setSpacing(10);
		information.setAlignment(Pos.CENTER);
		pane.getChildren().add(0, information);
		
		//~Initial Value
		HBox intialValue=new HBox();
		intialValue.setSpacing(10);
		intialValue.setAlignment(Pos.CENTER);
		information.getChildren().add(intialValue);
		//---
		intialValue.getChildren().add(new Label("Valor Inicial:"));
		//---
		Label intialValueVar=new Label(formatMoney(ppe.getValue()));
		intialValue.getChildren().add(intialValueVar);
		//~...
		
		//~Accumulated Depreciation
		HBox accumulatedDepreciation=new HBox();
		accumulatedDepreciation.setSpacing(10);
		accumulatedDepreciation.setAlignment(Pos.CENTER);
		information.getChildren().add(accumulatedDepreciation);
		//---
		accumulatedDepreciation.getChildren().add(new Label("Depreciacion Acumulada:"));
		//---
		Label accumulatedDepreciationVar=new Label(formatMoney(ppe.calculateAccumulatedDepreciation()));
		accumulatedDepreciation.getChildren().add(accumulatedDepreciationVar);
		//~...
		
		//~Accumulated Deterioration
		HBox accumulatedDeterioration=new HBox();
		accumulatedDeterioration.setSpacing(10);
		accumulatedDeterioration.setAlignment(Pos.CENTER);
		information.getChildren().add(accumulatedDeterioration);
		//---
		accumulatedDeterioration.getChildren().add(new Label("Deterioro Acumulado:"));
		//---
		Label accumulatedDeteriorationVar=new Label(formatMoney(ppe.calculateAccumulatedDeterioration()));
		accumulatedDeterioration.getChildren().add(accumulatedDeteriorationVar);
		//~...
		
		//~Accumulated Valorization
		HBox accumulatedValorization=new HBox();
		accumulatedValorization.setSpacing(10);
		accumulatedValorization.setAlignment(Pos.CENTER);
		information.getChildren().add(accumulatedValorization);
		//---
		accumulatedValorization.getChildren().add(new Label("Valorizacion Acumulado:"));
		//---
		Label accumulatedValorizationVar=new Label(formatMoney(ppe.calculateAccumulatedValorization()));
		accumulatedValorization.getChildren().add(accumulatedValorizationVar);
		//~...
		
		//~Net Value
		HBox netValue=new HBox();
		netValue.setSpacing(10);
		netValue.setAlignment(Pos.CENTER);
		information.getChildren().add(netValue);
		//---
		netValue.getChildren().add(new Label("Valor Neto:"));
		//---
		Label netValueVar=new Label(formatMoney(ppe.calculateNetValue()));
		netValue.getChildren().add(netValueVar);
		//~...
		
		//~Units
		HBox units=new HBox();
		units.setSpacing(10);
		units.setAlignment(Pos.CENTER);
		information.getChildren().add(units);
		//---
		units.getChildren().add(new Label("Unidades:"));
		//---
		Label unitsVar=new Label(ppe.getUnits()+"");
		units.getChildren().add(unitsVar);
		//~...
		
		//~Entrusted
		HBox entrusted=new HBox();
		entrusted.setSpacing(10);
		entrusted.setAlignment(Pos.CENTER);
		information.getChildren().add(entrusted);
		//---
		entrusted.getChildren().add(new Label("Encargado:"));
		//---
		Label entrustedVar=new Label(ppe.getEntrusted());
		entrusted.getChildren().add(entrustedVar);
		//~...
		
		//~Date
		HBox date=new HBox();
		date.setSpacing(10);
		date.setAlignment(Pos.CENTER);
		information.getChildren().add(date);
		//---
		date.getChildren().add(new Label("Dia Comprado:"));
		//---
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
		Label dateVar=new Label(f.format(ppe.getDate().getTime()));
		date.getChildren().add(dateVar);
		//~...
		
		//~Life Span
		HBox lifespan=new HBox();
		lifespan.setSpacing(10);
		lifespan.setAlignment(Pos.CENTER);
		information.getChildren().add(lifespan);
		//---
		lifespan.getChildren().add(new Label("Vida Util:"));
		//---
		Label lifespanVar=new Label(ppe.getLifespan()+" meses");
		lifespan.getChildren().add(lifespanVar);
		//~...
		
		//~Description
		VBox description=new VBox();
		description.setSpacing(10);
		description.setAlignment(Pos.CENTER);
		information.getChildren().add(description);
		//---
		description.getChildren().add(new Label("Descripcion:"));
		//---
		Label descriptionVar=new Label(ppe.getDescription());
		description.getChildren().add(descriptionVar);
		//~...
		
		//~Time Line
		Button timeline=new Button(TIMELINE_SYMBOL);
		information.getChildren().add(timeline);
		timeline.setOnMouseClicked(event->{
			generateTimeline();
		});
		//~...
		//...
		
	}
	
	public void generatePPECreator() {
		header.getChildren().clear();
		list.getItems().clear();
		pane.getChildren().remove(information);
		if(itemMenu!=null){itemMenu.hide();}
		header.setAlignment(Pos.CENTER);
		
		//HEADER
		//~Back
		Button back=new Button(BACK_SYMBOL);
		back.setOnMouseClicked(event->{
			identifiers[1]=null;
			generate();
		});
		//~...
		//~Game Name
		TextField ppeName=new TextField();
		ppeName.setPromptText("Nombre");
		ppeName.getStyleClass().add("title");
		//...
		
		//PPE
		information=new VBox();
		information.setSpacing(10);
		information.setAlignment(Pos.CENTER);
		pane.getChildren().add(0, information);
		
		//~Units
		HBox units=new HBox();
		units.setSpacing(10);
		units.setAlignment(Pos.CENTER);
		information.getChildren().add(units);
		//---
		units.getChildren().add(new Label("Unidades:"));
		//---
		TextField unitsVar=new TextField();
		units.getChildren().add(unitsVar);
		//~...
		
		//~Initial Value
		HBox intialValue=new HBox();
		intialValue.setSpacing(10);
		intialValue.setAlignment(Pos.CENTER);
		information.getChildren().add(intialValue);
		//---
		intialValue.getChildren().add(new Label("Valor Inicial:"));
		//---
		TextField intialValueVar=new TextField();
		intialValue.getChildren().add(intialValueVar);
		//~...
		
		//~Entrusted
		HBox entrusted=new HBox();
		entrusted.setSpacing(10);
		entrusted.setAlignment(Pos.CENTER);
		information.getChildren().add(entrusted);
		//---
		entrusted.getChildren().add(new Label("Encargado:"));
		//---
		TextField entrustedVar=new TextField();
		entrusted.getChildren().add(entrustedVar);
		//~...
		
		//~Date
		HBox date=new HBox();
		date.setSpacing(10);
		date.setAlignment(Pos.CENTER);
		information.getChildren().add(date);
		//---
		date.getChildren().add(new Label("Dia Comprado:"));
		//---
		DatePicker dateVar=new DatePicker();
		date.getChildren().add(dateVar);
		//~...
		
		//~Life Span
		HBox lifespan=new HBox();
		lifespan.setSpacing(10);
		lifespan.setAlignment(Pos.CENTER);
		information.getChildren().add(lifespan);
		//---
		lifespan.getChildren().add(new Label("Vida Util (En Meses):"));
		//---
		TextField lifespanVar=new TextField();
		lifespan.getChildren().add(lifespanVar);
		//~...
		
		//~Description
		VBox description=new VBox();
		description.setSpacing(10);
		description.setAlignment(Pos.CENTER);
		information.getChildren().add(description);
		//---
		description.getChildren().add(new Label("Descripcion:"));
		//---
		TextField descriptionVar=new TextField();
		description.getChildren().add(descriptionVar);
		//~...
		
		//...
		
		//~Add
		Button edit=new Button(DONE_SYMBOL);
		edit.setOnMouseClicked(event->{
			
			try {
				String nameP = ppeName.getText();
				int unitsP = Integer.parseInt(unitsVar.getText());
				double valueP = Double.parseDouble(intialValueVar.getText());
				int lifespanP = Integer.parseInt(lifespanVar.getText());
				GregorianCalendar dateP = GregorianCalendar.from(dateVar.getValue().atStartOfDay(ZoneId.systemDefault()));
				String entrustedP = entrustedVar.getText();
				String descriptionP = descriptionVar.getText();
				
				PPE ppe = new PPE(nameP, unitsP, valueP, lifespanP, dateP, entrustedP, descriptionP);
				
				if(!nameP.isEmpty() && !entrustedP.isEmpty()){
					accountant.searchCategory(identifiers[0]).addPPE(ppe);
					generate();
				}
				else {
					showAlert("Completa los espacios vacios");
				}
			}
			catch (AlreadyExistException e) {
				showAlert("Ya existe un PPE con ese nombre");
			}
			catch(NumberFormatException e){
				showAlert("Coloca datos validos en las casillas");
			}
			
		});
		//~...
		header.getChildren().addAll(back,ppeName,edit);
		//...
		
	}
	
	public void generateTimeline() {
		header.getChildren().clear();
		list.getItems().clear();
		pane.getChildren().remove(information);
		if(itemMenu!=null){itemMenu.hide();}
		header.setAlignment(Pos.CENTER);
		
		//HEADER
		//~Back
		Button back=new Button(BACK_SYMBOL);
		back.setOnMouseClicked(event->{
			generate();
		});
		//~...
		//~Console Name
		Label categoryName=new Label(identifiers[1].toString());
		categoryName.getStyleClass().add("title");
		//~...
		Label empty=new Label("");
		header.getChildren().addAll(back,categoryName,empty);
		//...
		
		information =new VBox();
		information.setSpacing(5);
		information.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(0, 0, 0, 0));
		information.setPadding(new Insets(10, 0, 0, 0));
		pane.getChildren().add(0, information);
		
		TableView<PPETableView> table = new TableView<>();
		table.setEditable(false);
		Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
		table.setPrefHeight(visualBounds.getHeight());
		table.setPrefWidth(visualBounds.getWidth());
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		createColumns(table);
		
		information.getChildren().add(table);
		
		addData(table);
	}
	
	public void generatePPEEditor() {
		header.getChildren().clear();
		list.getItems().clear();
		pane.getChildren().remove(information);
		if(itemMenu!=null){itemMenu.hide();}
		header.setAlignment(Pos.CENTER);
		
		//HEADER
		//~Back
		Button back=new Button(BACK_SYMBOL);
		back.setOnMouseClicked(event->{
			generate();
		});
		//~...
		//~Game Name
		TextField ppeName=new TextField(identifiers[1]);
		ppeName.setPromptText("Nombre");
		ppeName.getStyleClass().add("title");
		//...
		
		//PPE
		PPE ppe = accountant.searchCategory(identifiers[0]).searchPPE(identifiers[1]);
		
		information=new VBox();
		information.setSpacing(10);
		information.setAlignment(Pos.CENTER);
		pane.getChildren().add(0, information);
		
		//~Entrusted
		HBox entrusted=new HBox();
		entrusted.setSpacing(10);
		entrusted.setAlignment(Pos.CENTER);
		information.getChildren().add(entrusted);
		//---
		entrusted.getChildren().add(new Label("Encargado:"));
		//---
		TextField entrustedVar=new TextField(ppe.getEntrusted());
		entrusted.getChildren().add(entrustedVar);
		//~...
		
		//~Description
		HBox description=new HBox();
		description.setSpacing(10);
		description.setAlignment(Pos.CENTER);
		information.getChildren().add(description);
		//---
		description.getChildren().add(new Label("Descripcion:"));
		//---
		TextField descriptionVar=new TextField(ppe.getDescription());
		description.getChildren().add(descriptionVar);
		//~...
		
		//~Deterioration
		HBox deterioration=new HBox();
		deterioration.setSpacing(10);
		deterioration.setAlignment(Pos.CENTER);
		information.getChildren().add(deterioration);
		//---
		deterioration.getChildren().add(new Label("Deterioro (Solo si se desea):"));
		//---
		TextField deteriorationVar=new TextField();
		deterioration.getChildren().add(deteriorationVar);
		//~...
		
		//~Valorizations
		HBox valorizations=new HBox();
		valorizations.setSpacing(10);
		valorizations.setAlignment(Pos.CENTER);
		information.getChildren().add(valorizations);
		//---
		valorizations.getChildren().add(new Label("Valorizacion (Solo si se desea):"));
		//---
		TextField valorizationsVar=new TextField();
		valorizations.getChildren().add(valorizationsVar);
		//~...
		
		//...
		
		//~Add
		Button edit=new Button(DONE_SYMBOL);
		edit.setOnMouseClicked(event->{
			
			try {
				
				if((ppeName.getText().isEmpty()) || (entrustedVar.getText().isEmpty())){
					showAlert("Completa los espacios vacios");
				}
				else if((!deteriorationVar.getText().isEmpty()) && (Double.parseDouble(deteriorationVar.getText()) > 0)){
					showAlert("El deterioro solo pueden ser numeros negativos");
				}
				else {
					//Dat
					double deteriorationDat = 0;
					if(!deteriorationVar.getText().isEmpty()) {
						deteriorationDat = Double.parseDouble(deteriorationVar.getText());
					}
					
					double valorizationDat = 0;
					if(!valorizationsVar.getText().isEmpty()){
						valorizationDat = Double.parseDouble(valorizationsVar.getText());
					}
					if(deteriorationDat != 0 && !(ppe.getState().equals(State.OWNED)) ) {
						throw new NotOwnedException();
					}
					//Deterioration
					if(deteriorationDat != 0){
						ppe.addDeterioration(deteriorationDat);
					}
					//Valorization
					if(valorizationDat != 0){
						ppe.addValorizations(valorizationDat);
					}
					
					//Edit
					accountant.searchCategory(identifiers[0]).editPPE(ppe, ppeName.getText(), entrustedVar.getText(), descriptionVar.getText());
					identifiers[1] = ppe.getName();
					generate();
				}
			}
			catch (AlreadyExistException e) {
				showAlert("Ya existe un PPE con ese nombre");
			}
			catch(NumberFormatException e){
				showAlert("Coloca datos validos en las casillas");
			}
			catch (NotOwnedException e) {
				showAlert("No posees este item");
			}
			
		});
		//~...
		header.getChildren().addAll(back,ppeName,edit);
		//...
		
	}
	
	//Supporters
	@SuppressWarnings("unchecked")
	public void createColumns(TableView<PPETableView> tableView) {
		
		TableColumn<PPETableView, String> name = new TableColumn<>("Nombre");
		name.setMinWidth(177.5);
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		name.setSortable(false);
		
		TableColumn<PPETableView, String> date = new TableColumn<>("Fecha");
		date.setMinWidth(120);
		date.setCellValueFactory(new PropertyValueFactory<>("date"));
		date.setSortable(false);
		
		TableColumn<PPETableView, String> historic = new TableColumn<>("Valor Historico");
		historic.setMinWidth(200);
		historic.setCellValueFactory(new PropertyValueFactory<>("historic"));
		historic.setSortable(false);
		
		TableColumn<PPETableView, String> unitHistoric = new TableColumn<>("Valor Unitario Historico");
		unitHistoric.setMinWidth(250);
		unitHistoric.setCellValueFactory(new PropertyValueFactory<>("unitHistoric"));
		unitHistoric.setSortable(false);
		
		TableColumn<PPETableView, String> depretation = new TableColumn<>("Depreciacion Acumulada");
		depretation.setMinWidth(265);
		depretation.setCellValueFactory(new PropertyValueFactory<>("depretation"));
		depretation.setSortable(false);
		
		TableColumn<PPETableView, String> damaged = new TableColumn<>("Deterioro Acumulado");
		damaged.setMinWidth(270);
		damaged.setCellValueFactory(new PropertyValueFactory<>("damaged"));
		damaged.setSortable(false);
		
		TableColumn<PPETableView, String> valorization = new TableColumn<>("Valorizacion");
		valorization.setMinWidth(200);
		valorization.setCellValueFactory(new PropertyValueFactory<>("valorization"));
		valorization.setSortable(false);
		
		TableColumn<PPETableView, String> net = new TableColumn<>("Valor Neto");
		net.setMinWidth(215);
		net.setCellValueFactory(new PropertyValueFactory<>("net"));
		net.setSortable(false);
		
		TableColumn<PPETableView, String> unitNet= new TableColumn<>("Valor Unitario Neto");
		unitNet.setMinWidth(220);
		unitNet.setCellValueFactory(new PropertyValueFactory<>("unitNet"));
		unitNet.setSortable(false);
		
		tableView.getColumns().addAll(name, date, historic, unitHistoric, depretation, damaged, valorization, net, unitNet);
	}
	
	public void addData(TableView<PPETableView> tableView) {
		String[][] table = accountant.searchCategory(identifiers[0]).searchPPE(identifiers[1]).showDepreciationReport();
		ObservableList<PPETableView> data = FXCollections.observableArrayList();
		
		for(int i = 0; i < table.length; i++) {
			data.add(new PPETableView(table[i][0], table[i][1], formatMoney(Double.parseDouble(table[i][2])), formatMoney(Double.parseDouble(table[i][3])), formatMoney(Double.parseDouble(table[i][4])), formatMoney(Double.parseDouble(table[i][5])), formatMoney(Double.parseDouble(table[i][6])), formatMoney(Double.parseDouble(table[i][7])), formatMoney(Double.parseDouble(table[i][8]))));
		}
		
		tableView.setItems(data);
	}
	
	public String formatMoney(Object money) {
		NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
		String displayMoney = format.format(money);
		return displayMoney;
	}
	
	public TextField onActionAddButton(Button add){
		add.setText(MINIMIZE_SYMBOL);
		add.setOnMouseClicked(mEvent->{
			generate();
		});
		
		HBox itemBox=new HBox();
		itemBox.setSpacing(10);
		itemBox.setAlignment(Pos.CENTER);
		
		TextField itemName=new TextField();
		itemBox.getChildren().add(itemName);
		list.getItems().add(itemBox);
		
		return itemName;
	}
	
	public void showAlert(String message){
		ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
		Alert alert = new Alert(AlertType.NONE, message, ok);
		alert.setHeaderText(null);
		alert.setTitle(null);
		alert.initStyle(StageStyle.UNDECORATED);
		
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("/view/Style.css").toExternalForm());
		Stage stage = (Stage) dialogPane.getScene().getWindow();
		stage.getIcons().add(new Image("file:../../med/icon/Logo.png"));
		
		alert.showAndWait();
	}
	
	public void changeImageOpasity(ImageView image, boolean value){
		if(value){
			ColorAdjust colorAdjust = new ColorAdjust();
			colorAdjust.setBrightness(0);
			image.setEffect(colorAdjust);
		}
		else{
			ColorAdjust colorAdjust = new ColorAdjust();
			colorAdjust.setBrightness(-0.75);
			image.setEffect(colorAdjust);
		}
	}
	
	public HBox generateItemBox(String itemName, String imgPath,String defaultImgPath){
		HBox itemBox=new HBox();
		itemBox.setSpacing(10);
		itemBox.setAlignment(Pos.CENTER);
		itemBox.getStyleClass().add("item-box");
		
		//Image
		File img=new File(ICONS_PATH+imgPath);
		if(!img.exists()){
			img=new File(DEFAULT_ICONS_PATH+defaultImgPath);
		}
		try {
			String imgUrl=img.toURI().toURL().toString();
			itemBox.getChildren().add(new ImageView(new Image(imgUrl, 60, 60, false, true)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//...
		
		//NAME
		itemBox.getChildren().add(new Label(itemName));
		//...
		
		return itemBox;
	}
	
	public void generateItemMenu(){
		if(itemMenu!=null){
			itemMenu.hide();
		}
		itemMenu = new ContextMenu();
	}
	
	public void saveData(Stage stage){
		stage.setOnCloseRequest(event -> {
			accountant.save();
		});
	}
	
	public void generateSearchList(ArrayList<PPE> ppes, String label) {
		for(int i=0; i<ppes.size(); i++){
			PPE ppe = ppes.get(i);
			ppe.depreciated();
			HBox itemBox=generateItemBox(ppe.toString(), identifiers[0]+"/"+ppe.getName()+".png", "PPE.png");
			
			//State
			String state=ppe.getState().toString();
			itemBox.getChildren().add(new Label(state));
			//...
			
			//State
			itemBox.getChildren().add(new Label(label));
			//...
			
			//OnAction
			itemBox.setOnMouseClicked(event->{
				
				//Open
				if(event.getButton()==MouseButton.PRIMARY){
					boolean run = true;
					
					for(int j = 0; (j < accountant.getCategories().size()) && run; j++){
						for(int k = 0; (k < accountant.getCategories().get(j).getPpes().size()) && run; k++){
							
							if(accountant.getCategories().get(j).getPpes().get(k).equals(ppe)) {
								this.identifiers[0] = accountant.getCategories().get(j).getName();
								this.identifiers[1] = ppe.getName();
								
								run = false;
							}
							
						}
					}
					
					generate();
				}
				//...
				//Option Menu
				else if(event.getButton()==MouseButton.SECONDARY){
					
					generateItemMenu();
					//Sell
					MenuItem delete = new MenuItem("Vender");
			        delete.setOnAction(dEvent->{
			        	try {
							ppe.sell();
						}
			        	catch (NotOwnedException e) {
							showAlert("No posees este PPE");
						}
			        	generate();
			        });
					//...
			        
			        //Image
			        MenuItem image = new MenuItem("Cambiar Imagen");
			        image.setOnAction(eEvent->{
			        	//Choose File
			        	Stage stage = (Stage) pane.getScene().getWindow();
				        FileChooser fileChooser = new FileChooser();
						fileChooser.setTitle("Image Selector");
						fileChooser.getExtensionFilters().add(new ExtensionFilter("PNG", "*.png"));
						File imageFile=fileChooser.showOpenDialog(stage);
						//...
						//CreateDir
						new File(ICONS_PATH+identifiers[0]).mkdir();
						//...
						//Delete
						File newImageFile=new File(ICONS_PATH+identifiers[0]+"/"+ppe.getName()+".png");
						newImageFile.delete();
						//...
						//Change
						if(imageFile!=null){
							imageFile.renameTo(newImageFile);
						}
						generate();
						//...
						
			        });
			        //...
			        
			        itemMenu.getItems().addAll(delete, image);
			        itemMenu.show(itemBox, event.getScreenX(), event.getScreenY());
			        
				}
				//...
				
			});
			//...
			
			list.getItems().add(itemBox);
			
		}
	}
}