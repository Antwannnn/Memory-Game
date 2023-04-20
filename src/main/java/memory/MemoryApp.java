package memory;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import memory.om.Jeu;
import memory.view.GameScreenController;
import memory.view.HomeScreenController;

public class MemoryApp extends Application {
	
	private BorderPane errorScreen;
	
	private boolean isCheatModeToggled;
	
	private Jeu game;
	
	private Stage current;

	@Override
	public void start(Stage primaryStage){
		
		this.current = primaryStage;
			
		FXMLLoader loader = new FXMLLoader(MemoryApp.class.getResource("view/homescreen.fxml"));		

		Scene mainScene;
		
		try {
			 mainScene = new Scene(loader.load());
		}catch(Exception e) {
			errorScreen = new BorderPane();
			errorScreen.setCenter(new Label("Couldn't find targeted FXML file"));
			mainScene = new Scene(errorScreen);
		}
		
		HomeScreenController homeScreenController = loader.getController();
		homeScreenController.setApplication(this);
		this.isCheatModeToggled = homeScreenController.isCheatModeToggled();
		
		this.game = new Jeu(8, this.isCheatModeToggled);
		
		
		current.setScene(mainScene);
		current.setTitle("Memory nombre");
		current.show();
	}
	
	public void loadNewGame() throws IOException {
		
		FXMLLoader gameLoader = new FXMLLoader(MemoryApp.class.getResource("view/game.fxml"));
		
		Scene gameScene = new Scene(gameLoader.load());
		
		GameScreenController gameScreenController = gameLoader.getController();
		
		gameScreenController.initGrid(this.game);
				
		this.current.setScene(gameScene);
		this.current.setTitle("Jeu en cours");
	}
	
	public static void main2(String[] args) {
		Application.launch(args);
	}


}
