package view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * This class represents the screen that gets displayed when the user is trying to answer the question.
 * 
 * @author Sherman Chin, Dave Shin
 *
 */
public class PracticeAnswerScreen{
	
	private String _clue;
	private String[] _answers;
	private int _remainingAttempts;
	
	private BorderPane _pane;
	private Button _submitBtn;
	private Button _repeatBtn;
	private TextField _attemptInput;
	private Text _hint;
	private Text _wrongText;
	private Text _attemptsCountText;
	
	public PracticeAnswerScreen(BorderPane pane, String clue, String[] answers) {
		_clue = clue;
		_answers = answers;
		_remainingAttempts = 4; // The user is allowed four attempts at one question.
		
		_pane = pane;
		_submitBtn = new Button("Submit");
		_submitBtn.getStyleClass().add("golden-button");
				
		_attemptInput = new TextField();
		
		_repeatBtn = new Button("Repeat Clue");
		_repeatBtn.getStyleClass().add("golden-button");
		
		_hint = new Text("Hint: The first letter of the answer is \"" + _answers[0].charAt(0) + "\"");
		_hint.getStyleClass().addAll("normal-text", "invisible-component");
		
		_wrongText = new Text("Incorrect!");
		_wrongText.getStyleClass().addAll("header-msg", "normal-text", "invisible-component");
		
		_attemptsCountText = new Text("Number of attempts remaining: " + Integer.toString(_remainingAttempts));
		_attemptsCountText.getStyleClass().add("normal-text");
		
	}
	
	/**
	 * Lays out the GUI.
	 */
	public void display() {
		handleEvents();
		
		VBox pracAnsBox = new VBox();
		pracAnsBox.getStyleClass().add("center-screen-box");
		
		Text instruction = new Text("Clue: " + _clue);
		instruction.getStyleClass().addAll("normal-text", "information-text");
		instruction.setWrappingWidth(500);
		instruction.setTextAlignment(TextAlignment.CENTER);
		speak(_clue);
		
		HBox inputAndSoundBtn = new HBox();
		inputAndSoundBtn.getStyleClass().add("center-screen-box");
		inputAndSoundBtn.getChildren().addAll(_attemptInput, _repeatBtn);
		
		pracAnsBox.getChildren().addAll(_wrongText, instruction, inputAndSoundBtn, _submitBtn, _attemptsCountText, _hint);
		_pane.setCenter(pracAnsBox);
	}
	
	/**
	 * Adds listeners to buttons
	 */
	public void handleEvents() {
		_submitBtn.setOnAction(new EventHandler<ActionEvent>() {	
			@Override
			public void handle(ActionEvent arg0) {
				boolean correct = false;
				
				String attempt = _attemptInput.getText();
				
				// Make it case-insensitive and trim all leading and trailing spaces.
				attempt = attempt.toLowerCase();
				attempt = attempt.trim();
				
				PracticeSolutionScreen solScrn = new PracticeSolutionScreen(_pane, _clue, _answers[0]);
				
				// A for loop is used because there can be multiple solutions and we want to 
				// check if the attempt matches with at least one solution.
				for (String solution : _answers) {
					solution = solution.toLowerCase();
					solution = solution.trim();
					
					if (attempt.equals(solution)) {
						solScrn.displayCorrect();
						correct = true;
					}
				}
				
				if (!correct) {
					_remainingAttempts--;
					
					// Only add wrongText when two attempts remain to prevent from duplicate 
					// children from being added.
					_wrongText.getStyleClass().remove("invisible-component");
					speak("Incorrect");
					
					if (_remainingAttempts == 1) {
						_hint.getStyleClass().remove("invisible-component");
						speak(_hint.getText());
					} else if (_remainingAttempts < 1) {
						solScrn.displayIncorrect();
					}

					_attemptsCountText.setText("Number of attempts remaining: " + (_remainingAttempts));
				}
			}
		});
		
		_repeatBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				speak(_clue);
			}
		});
	}
	
	/**
	 * This method uses bash commands to use festival to speak any sentences.
	 * @param speech A string which will be spoken out using festival.
	 */
	public void speak(String speech) {
		
		// Bash command for speaking out the clue.
//		String speakClueCmd = "echo \"" + speech + "\" | festival --tts";
//		
//		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", speakClueCmd);
//		try {
//			Process process = builder.start();
//			process.toString();
//		}
//		catch (IOException e) {
//			System.out.println("Error with using festival to read out the question.");
//			e.printStackTrace();
//		}
	}
}
