package view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AnswerScreen {
	
	private Stage _primaryStage;
	private TextField _attempt;
	private Button _submitBtn;
	private Button _dontKnowBtn;
	
	private Question _question;
	
	public AnswerScreen(Stage primaryStage, Question question) {
		_primaryStage = primaryStage;
		_attempt = new TextField("Type your answer here");
		_submitBtn = new Button("Submit");
		_dontKnowBtn = new Button("Don\'t know");
		_question = question;
	}
	
	public void display() {
		GridPane answerPane = new GridPane();
		Text instruction = new Text();
		instruction.setText("Listen to the clue then answer the question.");
		answerPane.add(instruction, 0, 0);
		
		answerPane.add(_attempt, 0, 1);
		answerPane.add(_submitBtn, 0, 2);
		answerPane.add(_dontKnowBtn, 1, 2);
		
		handleEvents();
		
		_primaryStage.setScene(new Scene(answerPane, 600, 400));
		_primaryStage.show();
		
		System.out.println(_question); // Something is wrong with this.
		speakClue();
	}
	
	public void handleEvents() {
		_submitBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				System.out.println("Submit!");
			}
		});
		
		_dontKnowBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				GamesModule gamesMod = new GamesModule(_primaryStage);
        		gamesMod.display();
			}
		});
	}
	
	public void speakClue() {
		
		// Bash command for speaking out the clue.
		String speakClueCmd = "echo \"" + _question + "\" | festival --tts";
		
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", speakClueCmd);
		try {
			Process process = builder.start();
		}
		catch (IOException e) {
			System.out.println("Error with using festival to read out the question.");
			e.printStackTrace();
		}
	}
}