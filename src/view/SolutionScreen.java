package view;

import java.io.IOException;

import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Score;

/**
 * This class represents the screen that get shown after the user submits their answer.
 * 
 * @author Dave Shin
 *
 */
public class SolutionScreen {
	
	private Stage _primaryStage;
	private Button _returnBtn;
	private String _solution;
	private Score _score;
	private Question _question;
	
	public SolutionScreen(Stage primaryStage, Question question, String solution) {
		_primaryStage= primaryStage;
		_returnBtn = new Button("Return");
		_solution = solution;
		_score = new Score();
		_question = question;
	}
	
	public void displayCorrect() {
		Text correctMsg = new Text("Correct!");
//		
//		String sayCorrectCmd = "echo \"Correct\" | festival --tts";
//		
//		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", sayCorrectCmd);
//		try {
//			Process process = builder.start();
//			process.toString();
//		} catch (IOException e) {
//			System.out.println("Error with using festival to read out the question.");
//			e.printStackTrace();
//		}
		
		// Record the increased score.
		_score.updateScore(_question.getAmount());
		
		setUpAndShow(correctMsg);
	}
	
	public void displayIncorrect() {
		
		// Record the decreased winnings.
		_score.updateScore(-_question.getAmount());
		
		displayDontKnow();
	}
	
	public void displayDontKnow() {
		Text incorrectMsg = new Text("The actual answer is: " + _solution);
		
		// Sound out to the user that their attempt is incorrect and tell them the correct answer.
//		String sayIncorrectCmd = "echo \"The actual answer is " + _solution + "\" | festival --tts";		
//		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", sayIncorrectCmd);
//		try {
//			Process process = builder.start();
//			process.toString();
//		} catch (IOException e) {
//			System.out.println("Error with using festival to read out the question.");
//			e.printStackTrace();
//		}
//		
		setUpAndShow(incorrectMsg);
	}
	
	public void setUpAndShow(Text msg) {
		handleEvents();
		
		GridPane _solutionPane = new GridPane();
		_solutionPane.add(msg, 0, 0);
		_solutionPane.add(_returnBtn, 0, 1);
		
		_primaryStage.setScene(new Scene(_solutionPane, 600, 400));
		_primaryStage.show();
	}
	
	public void handleEvents() {
		_returnBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
        	public void handle(ActionEvent event) {
				GamesModule gamesMod = new GamesModule(_primaryStage);
        		gamesMod.display();
        		System.out.println(_score.getScore());
        	}
		});
	}
}
