package controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Accountant;

public class ControlMenu implements Initializable{
	
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
		accountant=new Accountant();
		identifiers=new String[2];
	}
	
	//Generators
	public void generate() {
		
		header.getChildren().clear();
		list.getItems().clear();
		pane.getChildren().remove(information);
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

	public void generateAccountant() {
		
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
        			generate();
        		}
        	});
		});
		//~...
		
		//~APP NAME
		Label accountantName=new Label("Empresa Farmaceutica Ossim");
		accountantName.getStyleClass().add("title");
		//~...
		
		//~ADD
		Button add=new Button(ADD_SYMBOL);
		add.setOnMouseClicked(event->{
			
			TextField categoryName=onActionAddButton(add);
			categoryName.setOnKeyPressed(kEvent->{
				
        		if(kEvent.getCode().equals(KeyCode.ENTER)){
        			generate();
        		}
        		
        	});
		});
		//~...
		header.getChildren().addAll(search,accountantName, add);
		//...
		
		//Example
		HBox itemBox=generateItemBox("Maquina", "Maquina.png", "Category.png");
		itemBox.setOnMouseClicked(event->{
			//Open
			if(event.getButton()==MouseButton.PRIMARY){
				identifiers[0]="Maquina";
				generate();
			}
		});
		list.getItems().add(itemBox);
		//...
		
	}
	
	public void generateCategory() {//Example
		
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
			generatePPEEditor();
		});
		//~...
		header.getChildren().addAll(back,categoryName,add);
		//...	
		
		//Example
		HBox itemBox=generateItemBox("Centrifuga", "Maquina/Centrifuga.png", "PPE.png");
		String money="$"+172800;
		itemBox.getChildren().add(new Label(money));
		itemBox.setOnMouseClicked(event->{
			//Open
			if(event.getButton()==MouseButton.PRIMARY){
				identifiers[1]="Centrifuga";
				generate();
			}
		});
		list.getItems().add(itemBox);
		//...
		
	}
	
	public void generatePPE() {//Example
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
		
		//Example
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
		Label intialValueVar=new Label("$"+480000);
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
		Label accumulatedDepreciationVar=new Label("($"+307200+")");
		accumulatedDepreciation.getChildren().add(accumulatedDepreciationVar);
		//~...
		
		//~Net Value
		HBox netValue=new HBox();
		netValue.setSpacing(10);
		netValue.setAlignment(Pos.CENTER);
		information.getChildren().add(netValue);
		//---
		netValue.getChildren().add(new Label("Valor Neto:"));
		//---
		Label netValueVar=new Label("$"+172800);
		netValue.getChildren().add(netValueVar);
		//~...
		
		
		//~Entrusted
		HBox entrusted=new HBox();
		entrusted.setSpacing(10);
		entrusted.setAlignment(Pos.CENTER);
		information.getChildren().add(entrusted);
		//---
		entrusted.getChildren().add(new Label("Encargado:"));
		//---
		Label entrustedVar=new Label("Mateo Valdes");
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
		Label dateVar=new Label("9 de Noviembre del 2020");
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
		Label lifespanVar=new Label("25 meses");
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
		Label descriptionVar=new Label("Mantener en sitios frios");
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
	
	public void generatePPEEditor() {//Example
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
		ppeName.getStyleClass().add("title");
		//...
		//~Add
		Button edit=new Button(DONE_SYMBOL);
		edit.setOnMouseClicked(event->{
			generate();
		});
		//~...
		header.getChildren().addAll(back,ppeName,edit);
		//...
		
		//Example
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
		TextField intialValueVar=new TextField();
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
		TextField accumulatedDepreciationVar=new TextField();
		accumulatedDepreciation.getChildren().add(accumulatedDepreciationVar);
		//~...
		
		//~Net Value
		HBox netValue=new HBox();
		netValue.setSpacing(10);
		netValue.setAlignment(Pos.CENTER);
		information.getChildren().add(netValue);
		//---
		netValue.getChildren().add(new Label("Valor Neto:"));
		//---
		TextField netValueVar=new TextField();
		netValue.getChildren().add(netValueVar);
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
		TextField dateVar=new TextField();
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
		
		if(identifiers[1]!=null){
			ppeName.setText("Centrifuga");
			intialValueVar.setText("480000");
			accumulatedDepreciationVar.setText("307200");
			netValueVar.setText("172800");
			entrustedVar.setText("Mateo Valdes");
			dateVar.setText("9 de Noviembre del 2020");
			lifespanVar.setText("25");
			descriptionVar.setText("Mantener en sitios frios");
		}
		//...
				
	}
	
	public void generateTimeline() {//Example
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
		
		for(int m=25; m>=0; m--){
			int money=(int)((480000)*((double)m/25));
			
			int month=11+(25-m);
			
			int year=18;
			while(month>12) {
				year+=1;
				month-=12;
			}
			
			HBox itemBox=new HBox();
			itemBox.setSpacing(10);
			itemBox.setAlignment(Pos.CENTER);
			itemBox.getStyleClass().add("item-box");
			
			itemBox.getChildren().addAll(new Label("1/"+month+"/"+year), new Label(), new Label("$"+money));
			list.getItems().add(itemBox);
		}
	}
	
	//Supporters
	public void editPPE() {
		
	}
	
	public void saveData(Stage stage){
		stage.setOnCloseRequest(event -> {
			
		});
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
	
}
