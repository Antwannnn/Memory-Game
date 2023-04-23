package memory.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import memory.MemoryApp;
import memory.om.Jeu;
import memory.om.Reponse;
import memory.view.enums.CardType;
import memory.view.enums.MatchResult;
import memory.view.enums.PlayerTurn;
import memory.view.interfaces.Applicable;

public class GameScreenController implements Initializable, Applicable {


	
	// GridPane principale du jeu, contenant toutes les ImageView.
	@FXML
	private GridPane gamePane = new GridPane();

	// Label du texte du joueur 1, sera défini à "Essais:" en mode 1 joueur
	@FXML
	private Label playerOneDisplayText;

	// Label du score du joueur 1
	@FXML
	private Label playerOneScore;

	// Label du texte du joueur 2, sera invisible en mode 1 joueur
	@FXML
	private Label playerTwoDisplayText;

	// Label du score du joueur 2, invisible et constant en mode 1 joueur. 
	@FXML
	private Label playerTwoScore;
	
	// Liste de toute les ImageView correspondant aux cartes à retourner.
	private ArrayList<ImageView> images;

	// Application principale
	private MemoryApp application;

	// PlayerTurn permettant d'organiser les tours.
	private PlayerTurn turn;

	// CardType permettant de définir quelle type de carte est sélectionné
	private CardType cardType;

	// ImageView de la première image sélectionnée
	private ImageView previous;

	// Boolean permettant d'éviter le click spam
	private boolean canClick;

	// Boolean permettant de déterminer si la partie est en mode multijoueur
	private boolean multiplayerEnabled;

	// Jeu principal utilisé pour toute la logique de la partie
	private Jeu game;


	// Initialisation de la grille
	public void initGrid(Jeu game){
		// Définit le jeu comme celui renseigné (qui a été modifié selon les paramètres du menu principal)
		this.game = game;
		this.images = new ArrayList<>();
		// Par défaut, les joueurs peuvent cliquer.
		this.canClick = true;
		// Si le mode multijoueur est activé on rend visible le score du joueur 2 et on donne le premier tout au joueur 1
		if(multiplayerEnabled) {
			playerTwoScore.setVisible(true);
			playerTwoDisplayText.setVisible(true);
			playerTwoScore.setOpacity(0.5);
			playerTwoDisplayText.setOpacity(0.5);
			playerOneDisplayText.setText("Joueur 1:");
			this.turn = PlayerTurn.PLAYERONE;
		} 
		// Sinon le tour est défini à NONE pour éviter toute interférence entre mode 1 joueur et mode 2 joueurs
		else {
			this.turn = PlayerTurn.NONE;
		}
		
		this.fillImageViewList();
		this.loadImages();
	}

	// Rempli la liste d'ImageView avec les enfants de la grid pane.
	private void fillImageViewList() {
		// For each prenant chaque node enfant de la grid pane principale
		for(Node n : gamePane.getChildren()) {
			// Vérifie que l'enfant est bien une ImageView
			if(n instanceof ImageView) {
				// Par défaut, définit son opacité à 0 pour la cacher
				n.setOpacity(0);
				// Ajout à la liste
				images.add((ImageView)n);
			}
		}
	}

	// Méthode associée aux deux boutons relatifs à l'annulation de la partie ("Quitter" et "Retourner au menu principal")
	@FXML
	protected void onCancelButtonClick(ActionEvent event) {
		// Vérification que la source de l'event (l'élément cliqué) est bien un bouton
		if(event.getSource() instanceof Button) {
			Button current = (Button) event.getSource();
			// Création d'une nouvelle alerte permettant de demander la confirmation pour quitter la partie
			Alert quittingAlert = new Alert(AlertType.CONFIRMATION);
			quittingAlert.setTitle("Êtes-vous sûr ?");
			quittingAlert.initStyle(StageStyle.UNDECORATED);
			// Change le header text en fonction du bouton cliqué (donc du texte de ce bouton)
			if(current.getText().equals("Quitter")) {
				quittingAlert.setHeaderText("Quitter le jeu ?");
			} else {
				quittingAlert.setHeaderText("Retourner au menu principal ?");
			}
			Optional<ButtonType> types = quittingAlert.showAndWait();
			// Si le joueur appuie sur "OK" il quitte la partie / retourne au menu toujours en fonction du bouton cliqué
			if(types.get() == ButtonType.OK) {
				if(current.getText().equals("Quitter")) {
					application.stop();
				} else {
					try {
						application.loadMainMenu();
					} catch (IOException e) {
						System.out.println("Couldn't find specified view file.");
					}
				}

			}
		}
	}

	// Méthode retournant le gagnat en comparant les scores des joueurs
	// Retourne MatchResult.NONE si le mode multijoueur n'est pas activé
	private MatchResult getWinner() {
		if(multiplayerEnabled) {
			if(Integer.parseInt(playerOneScore.getText()) < Integer.parseInt(playerTwoScore.getText()))
				return MatchResult.PLAYERTWOWINS;
			else if(Integer.parseInt(playerOneScore.getText()) > Integer.parseInt(playerTwoScore.getText()))
				return MatchResult.PLAYERONEWINS;
			else if(Integer.parseInt(playerOneScore.getText()) == Integer.parseInt(playerTwoScore.getText()))
				return MatchResult.TIE;
			else
				return MatchResult.NONE;
		} else {
			return MatchResult.NONE;
		}

	}

	// Permet de définir quel type de carte sera utilisé lors du jeu
	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	// Permet de définir si oui ou non le mode multijoueur est activé
	public void setMultiplayerEnabled(boolean multiplayerEnabled) {
		this.multiplayerEnabled = multiplayerEnabled;
	}

	// Charge les images en fonction du CardType choisi tout en associant chaque cartes identiques en utilisant les valeurs de carte du Jeu.
	// Pour chaque ImageView, vérifie la valeur de sa carte pour lui associer l'image correspondante.
	// Tout cela en vérifiant à chaque fois le type de carte choisi
	private void loadImages() {
		for(int i = 0; i < images.size(); i++) {
			switch(game.getCarteValeur(i)) {
			case 0: 
				if(cardType.equals(CardType.LANGUAGES))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/languages/c++.png")));
				else if(cardType.equals(CardType.LETTERS))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/letters/letter-i.png")));
				else if(cardType.equals(CardType.COLORS))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/colors/red.png")));
				else
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/numbers/number-8.png")));
				break;
			case 1:
				if(cardType.equals(CardType.LANGUAGES))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/languages/c-sharp.png")));
				else if(cardType.equals(CardType.LETTERS))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/letters/letter-g.png")));
				else if(cardType.equals(CardType.COLORS))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/colors/blue.png")));
				else
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/numbers/number-7.png")));
				break;
			case 2:
				if(cardType.equals(CardType.LANGUAGES))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/languages/java.png")));
				else if(cardType.equals(CardType.LETTERS))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/letters/letter-f.png")));
				else if(cardType.equals(CardType.COLORS))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/colors/yellow.png")));
				else
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/numbers/number-6.png")));
				break;
			case 3:
				if(cardType.equals(CardType.LANGUAGES))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/languages/js.png")));
				else if(cardType.equals(CardType.LETTERS))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/letters/letter-e.png")));
				else if(cardType.equals(CardType.COLORS))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/colors/pink.png")));
				else
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/numbers/number-5.png")));
				break;
			case 4:
				if(cardType.equals(CardType.LANGUAGES))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/languages/c.png")));
				else if(cardType.equals(CardType.LETTERS))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/letters/letter-d.png")));
				else if(cardType.equals(CardType.COLORS))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/colors/white.png")));
				else
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/numbers/number-4.png")));
				break;
			case 5:
				if(cardType.equals(CardType.LANGUAGES))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/languages/php.png")));
				else if(cardType.equals(CardType.LETTERS))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/letters/letter-c.png")));
				else if(cardType.equals(CardType.COLORS))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/colors/orange.png")));
				else
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/numbers/number-3.png")));
				break;
			case 6:
				if(cardType.equals(CardType.LANGUAGES))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/languages/python.png")));
				else if(cardType.equals(CardType.LETTERS))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/letters/letter-b.png")));
				else if(cardType.equals(CardType.COLORS))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/colors/green.png")));
				else
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/numbers/number-2.png")));
				break;
			case 7:
				if(cardType.equals(CardType.LANGUAGES))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/languages/fortran.png")));
				else if(cardType.equals(CardType.LETTERS))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/letters/letter-a.png")));
				else if(cardType.equals(CardType.COLORS))
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/colors/purple.png")));
				else
					this.images.get(i).setImage(new Image(getClass().getResourceAsStream("/memory/view/images/numbers/number-1.png")));
				break;

			}	
		}
	}

	// Méthode associée à l'action de click sur n'importe quelle ImageView.
	@FXML
	protected void onGuessButtonClick(MouseEvent event) {
		
		// Vérification de si le joueur cliquer
		if(canClick) {

			// Récupération de l'image cliquée
			ImageView current = (ImageView) event.getPickResult().getIntersectedNode();
			
			// Création d'un nouveau timer pour pouvoir, par la suite, laisser 1 seconde au joueur pour voir les cartes et empêcher le click spam
			Timer time = new Timer();
			
			// Initialisation de l'indice de la liste d'ImageView avec lequel on va jouer
			int tries = -1;

			// On parcours la liste d'ImageView et on s'arrête dès qu'on trouve la bonne image.
			for(int i = 0; i < images.size(); i++) {
				if(images.get(i).equals(current)) {
					tries = i;
				}
			}

			// On récupère la Reponse avec l'indice
			Reponse answer = game.jouer(tries);
			
			// On affice la carte cliquée
			current.setOpacity(1);

			// On traite cas par cas les valeurs de réponse
			switch(answer) {

			// Si ce sont les deux mêmes cartes on ajoute un Essais en mode 1 joueur et une paire trouvée en mode 2 joueurs
			// On baisse également l'opacité des deux cartes trouvées
			case GAGNE:
				previous.setOpacity(0.5);
				current.setOpacity(0.5);
				previous = null;
				this.addFoundPeerCount();
				this.addTryCount();
				break;
			
			// Si c'est la première on la définit en tant que tel
			case PREMIERE:
				previous = current;
				break;

			// Si c'est perdu on désactive la possibilité de click
			case PERDU:
				canClick = false;
				// Prévision d'une tâche qui va, au bout d'une seconde, faire disparaitre les cartes et rendre au joueur la possibilité de cliquer
				time.schedule(new TimerTask() {

					@Override
					public void run() {
						current.setOpacity(0);
						canClick = true;
						previous.setOpacity(0);
						
					}
				}, 1000);
				
				this.addTryCount();
				break;

			default:
				previous = current;
				break;

			}

			// Quand la partie se termine, on dit à l'application principale d'afficher l'écran de victoire.
			if(game.isPartieTerminee()) {
				try {

					application.showWinScreen(Integer.parseInt(playerOneScore.getText()), getWinner());
					
				} catch (IOException e) {
					System.out.println("Couldn't find specified view file.");
				}
			}
		}
	}

	// Méthode ajoutant un essai au compteur en mode 1 joueurs
	// Change l'opacité des textes correspondant à chaque joueur pour préciser à qui est ce le tour et passe au tour suivant en mode 2 joueurs
	private void addTryCount() {
		if(turn.equals(PlayerTurn.NONE)) {
			int value = Integer.parseInt(playerOneScore.getText());
			value += 1;
			playerOneScore.setText("" + value);
		} else if(turn.equals(PlayerTurn.PLAYERONE)) {
			playerTwoScore.setOpacity(1);
			playerTwoDisplayText.setOpacity(1);
			playerOneScore.setOpacity(0.5);
			playerOneDisplayText.setOpacity(0.5);
			turn = PlayerTurn.PLAYERTWO;
		} else {
			playerOneScore.setOpacity(1);
			playerOneDisplayText.setOpacity(1);
			playerTwoScore.setOpacity(0.5);
			playerTwoDisplayText.setOpacity(0.5);
			turn = PlayerTurn.PLAYERONE;
		}

	}
	
	// Permet d'ajouter 1 au compteur de nombre de pairs trouvées
	private void addFoundPeerCount() {
		int value;
		if(turn.equals(PlayerTurn.PLAYERONE)) {
			value = Integer.parseInt(playerOneScore.getText());
			value += 1;
			playerOneScore.setText("" + value);

		}
		else if(turn.equals(PlayerTurn.PLAYERTWO)) {
			value = Integer.parseInt(playerTwoScore.getText());
			value += 1;
			playerTwoScore.setText("" + value);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}
	// Méthode héritée de Applicable précisant quelle application doit être utilisée en champ.
	@Override
	public void setApplication(Application app) {
		this.application = (MemoryApp) app;

	}



}
