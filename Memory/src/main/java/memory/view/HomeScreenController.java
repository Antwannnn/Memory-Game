package memory.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import memory.MemoryApp;
import memory.view.enums.CardType;
import memory.view.interfaces.Applicable;

public class HomeScreenController implements Initializable, Applicable{
	
	// Déclaration des champs
	
	// Bouton de nouvelle partie
	@FXML
	private Button newGameButton;
	
	// Bouton de fermeture de l'application
	@FXML
	private Button quitButton;
	
	// Radio button du mode triche
	@FXML
	private RadioButton cheatMode;
	
	// Radio button du mode langage
	@FXML
	private RadioButton languages;
	
	// Radio button du mode number
	@FXML
	private RadioButton numbers;
	
	// Radio button du mode letters
	@FXML
	private RadioButton letters;
	
	// Radio button du mode colors
	@FXML
	private RadioButton colors;
	
	@FXML
	private RadioButton onePlayer;
	
	// Radio button du mode 2 joueurs
	@FXML
	private RadioButton twoPlayers;
	
	// Application principale
	private MemoryApp application;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Initialized !");
	}
	
	// Demande à l'interface graphique si le mode triche est activé en verifiant que le bouton de triche est selectionné ou non
	public boolean isCheatModeToggled() {
		return cheatMode.isSelected();
	}
	
	// Méthode associée à l'action de click sur le bouton "Nouvelle partie"
	@FXML
	protected void onNewGameButtonClick() {
		try {
			// Passe par l'application principale pour appeler la fonction loadNewGame lançant une nouvelle partie.
			application.loadNewGame();
		} catch(IOException e) {
			e.printStackTrace();
		}

	}
	
	// Méthode associée à l'action de click sur le bouton "Quitter"
	@FXML
	protected void onQuitButtonClick() {
		application.stop();
	}
	
	// Méthode héritée de Applicable précisant quelle application doit être utilisée en champ.
	@Override
	public void setApplication(Application app) {
		this.application = (MemoryApp) app;
		
	}
	
	// Retourne le type de carte choisi en fonction du Radio button sélectionné
	public CardType getCardTypeByRadioButtonSelection() {
		if(letters.isSelected()) 
			return CardType.LETTERS;
		else if(languages.isSelected())
			return CardType.LANGUAGES;
		else if(colors.isSelected())
			return CardType.COLORS;
		else
			return CardType.NUMBERS;
	}
	
	// Retourne si oui ou non le mode 2 joueurs est activé en fonction de la sélection du radio button "2 joueurs"
	public boolean isMultiplayerEnabled() {
		return twoPlayers.isSelected();
	}


}
