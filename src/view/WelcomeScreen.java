package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WelcomeScreen {

	private Stage _primaryStage;

	private Button _pracModBtn;
	private Button _gamesModBtn;

	public WelcomeScreen(Stage primaryStage) {
		_primaryStage = primaryStage;

		_pracModBtn = new Button("The Practice Module");
		_pracModBtn.getStyleClass().add("golden-btn");
		_gamesModBtn = new Button("The Games Module");
		_gamesModBtn.getStyleClass().add("normal-btn");
		
		// Rest of this constructor are event handlers.
		_pracModBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Practice Module");
			}
		});

		_gamesModBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				GamesModule gamesMod = new GamesModule(_primaryStage);
				gamesMod.display();
			}
		});
	}

	public void display() {
		BorderPane welcomePane = new BorderPane();
		welcomePane.getStyleClass().add("background-screen");
		
		Text welcomeMessage = new Text("Welcome to Quinzical!");
		welcomeMessage.getStyleClass().addAll("welcome-msg", "normal-text");
		
		Text optionsText = new Text("Please select from one of the following options:");
		optionsText.getStyleClass().add("normal-text");
		// Display the main menu.
		VBox welcomeBox = new VBox();
		welcomeBox.getChildren().addAll(welcomeMessage, optionsText, _pracModBtn, _gamesModBtn);
		welcomeBox.getStyleClass().add("welcome-screen-box");
		VBox.setMargin(welcomeMessage, new Insets(0, 0, 15, 0));
		
		welcomePane.setCenter(welcomeBox);
		
		Scene welcomeScene = new Scene (welcomePane,600,400);
		
		welcomeScene.getStylesheets().add("view/application.css");
		_primaryStage.setScene(welcomeScene);
		_primaryStage.show();
	}
}

