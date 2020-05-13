package view;

import controller.ControlMenu;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application{

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Menu.fxml"));//FXML
			BorderPane root = (BorderPane) loader.load();
			root.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());//CSS
			Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
			root.setPrefHeight(visualBounds.getHeight());
			root.setPrefWidth(visualBounds.getWidth());
			primaryStage.getIcons().add(new Image("file:../../med/icon/default/logo.png"));
			primaryStage.setTitle("PPE Manager");
			primaryStage.setScene(new Scene(root, visualBounds.getWidth(), visualBounds.getHeight()));
			primaryStage.setMaximized(true);
			primaryStage.setMinHeight(visualBounds.getHeight());
			primaryStage.setMinWidth(visualBounds.getWidth());
			primaryStage.show();
			
			ControlMenu nextController = loader.getController();
			nextController.generate();
			nextController.saveData(primaryStage);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
