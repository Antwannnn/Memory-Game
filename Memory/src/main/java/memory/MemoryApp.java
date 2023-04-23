package memory;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import memory.om.Jeu;
import memory.view.GameScreenController;
import memory.view.HomeScreenController;
import memory.view.WinScreenController;
import memory.view.enums.MatchResult;

public class MemoryApp extends Application {
	
	private boolean isCheatModeToggled;
	
	private HomeScreenController homeScreenController;
	
	private Stage winWindow;
	
	private Jeu game;
	
	private Stage current;

	// Méthode principale lançant l'application
	
	@Override
	public void start(Stage primaryStage){
		
		this.current = primaryStage;
		this.current.initStyle(StageStyle.UNDECORATED);  
		
		try {
			loadMainMenu();
		} catch (IOException e) {
			System.out.println("Couldn't find specified view file.");
		}
			
		
	}
	
	// Méthode permettant de charger le menu principal à travers son document FXML
	public void loadMainMenu() throws IOException {
		
		// Initialisation du document FXML avec un constructeur paramétré prenant le chemin relatif vers le document fxml en paramètre
		FXMLLoader loader = new FXMLLoader(MemoryApp.class.getResource("view/homescreen.fxml"));	

		// Initialisation de la scène en chargeant le contenu du fxml loader
		Scene mainScene = new Scene(loader.load());
		
		// Récupération du controller du document fxml
		homeScreenController = loader.getController();
		
		// Précise à la classe du controller qu'il faut agir sur cette classe Application pour en récupérer les méthodes
		homeScreenController.setApplication(this);
	
		current.setScene(mainScene);
		current.setTitle("Memory !");
		current.show();
	}
	
	
	// Méthode permettant de charger une nouvelle partie.
	public void loadNewGame() throws IOException {
		
		FXMLLoader gameLoader = new FXMLLoader(MemoryApp.class.getResource("view/game.fxml"));
		
		Scene gameScene = new Scene(gameLoader.load());
		
		GameScreenController gameScreenController = gameLoader.getController();
		
		gameScreenController.setApplication(this);
		
		// Récupération du type de carte séléctionné dans le controller du home screen
		gameScreenController.setCardType(homeScreenController.getCardTypeByRadioButtonSelection());
		
		// Récupération de si oui ou non le mode multijoueur est activé
		gameScreenController.setMultiplayerEnabled(homeScreenController.isMultiplayerEnabled());
		
		// Récupération de si oui ou non le mode triche a été activé dans le home screen controller
		this.isCheatModeToggled = homeScreenController.isCheatModeToggled();
		// Instanciation d'un nouvel objet Jeu avec les paramètres adéquat
		this.game = new Jeu(8, isCheatModeToggled);
		
		// Initialisation de la grille de jeu.
		gameScreenController.initGrid(this.game);
				
		this.current.setScene(gameScene);
		this.current.setTitle("Jeu en cours");
	}
	
	// Méthode permettant de charger le menu de fin de jeu
	public void showWinScreen(final int score, MatchResult result) throws IOException {
		
		winWindow = new Stage();
		
		winWindow.initStyle(StageStyle.UNDECORATED);
		
		FXMLLoader winScreenLoader = new FXMLLoader(MemoryApp.class.getResource("view/winscreen.fxml"));
		
		Scene winScene = new Scene(winScreenLoader.load());
		
		WinScreenController winScreenController = winScreenLoader.getController();
		
		// Récupère le gagnant du jeu pour le préciser au WinScreenController
		winScreenController.setWinner(result);
		// Récupère le score pour le préciser au WinScreenController
		winScreenController.setScore(score);
		
		winScreenController.setApplication(this);
		
		winWindow.setTitle("Fin de partie.");
		winWindow.setScene(winScene);
		winWindow.show();
	}
	
	// Méthode permettant de fermer le menu de fin de jeu
	public void closeWinScreen() {
		if(this.winWindow.isShowing()) {
			this.winWindow.close();
		}
	}
	
	// Méthode permettant d'arrêter l'application
	public void stop() {
        Platform.exit();
        System.exit(0);	
	}
	
	public static void main2(String[] args) {
		Application.launch(args);
	}


}
