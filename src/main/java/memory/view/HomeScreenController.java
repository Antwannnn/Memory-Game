package memory.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import memory.MemoryApp;
import memory.om.Jeu;

public class HomeScreenController implements Initializable {
	
	@FXML
	private Button newGameButton;
	
	private MemoryApp application;
	
	@FXML
	private RadioButton cheatMode;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Initialized !");
	}
	
	public void setApplication(MemoryApp app) {
		this.application = app;
	}
	
	public boolean isCheatModeToggled() {
		return cheatMode.isSelected();
	}
	
	@FXML
	protected void onNewGameButtonClick() {
		try {
			application.loadNewGame();
		} catch(IOException e) {
			e.printStackTrace();
		}

	}

}
