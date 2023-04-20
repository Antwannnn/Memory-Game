package memory.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import memory.om.Jeu;

public class GameScreenController implements Initializable {
	
	@FXML
	private GridPane gamePane = new GridPane();
	
	private ArrayList<Button> buttons;
	
	private Jeu game;
	
	public void initGrid(Jeu game){
		this.game = game;
		this.buttons = new ArrayList<>();
		this.fillButtonList();
		this.applyNumbersToButtonList();
	}
	
	private void fillButtonList() {
		for(Node n : gamePane.getChildren()) {
			if(n instanceof Button) {
				buttons.add((Button) n);
			}
		}
	}
	
	private void applyNumbersToButtonList() {
		for(int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setText("" + game.getCarteValeur(i));
		}
	}
	
	


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	

}
