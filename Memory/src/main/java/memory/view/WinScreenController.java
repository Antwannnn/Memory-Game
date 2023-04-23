package memory.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import memory.MemoryApp;
import memory.view.enums.MatchResult;
import memory.view.interfaces.Applicable;

public class WinScreenController implements Initializable, Applicable {

	// Label du score final. Non affiché en mode multijoueur
	@FXML
	private Label score;
	
	// Label du texte à côté du nombre de coups joués. Affiche le joueur gagnant en mode multijoueur
	@FXML
	private Label displayText;
	
	// MatchResult du résultat final du match
	private MatchResult result;

	// Application principale.
	private MemoryApp application;
	
	/*Méthode définissant le score 
	 * Affiche le score en mode 1 joueur
	 * Affiche le gagnant en mode multijoueur
	 */
	public void setScore(int score) {
		if(result.equals(MatchResult.NONE))
			this.score.setText("" + score);
		else {
			String winner = "";
			if(result.equals(MatchResult.PLAYERONEWINS))
				winner = "Joueur 1 gagne la partie !";
			else if(result.equals(MatchResult.PLAYERTWOWINS))
				winner = "Joueur 2 gagne la partie !";
			else if(result.equals(MatchResult.TIE))
				winner = "Egalité !";
			
			this.score.setVisible(false);
			this.displayText.setText(winner);

		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		

	}

	// Méthode associée à l'action de click des boutons de la page
	// Exécute des actions différentes en fonction du texte des boutons
	@FXML
	protected void onButtonClick(ActionEvent event) {
		if(event.getSource() instanceof Button) {
			Button clicked = (Button) event.getSource();
			try {
				// Relance la partie
				if(clicked.getText().equals("Relancer !")) {
					application.loadNewGame();
				} 
				else {
					// Fait revenir au menu principal de l'application
					application.loadMainMenu();
				}
				application.closeWinScreen();
			}
			catch (IOException e) {
				System.out.println("Couldn't find specified view file.");
			}
		}
	}
	
	// Permet de définir le gagnant pour l'afficher sur l'écran de victoire
	public void setWinner(MatchResult result) {
		this.result = result;
	}

	// Méthode héritée de Applicable précisant quelle application doit être utilisée en champ.
	@Override
	public void setApplication(Application app) {
		this.application = (MemoryApp) app;

	}



}
