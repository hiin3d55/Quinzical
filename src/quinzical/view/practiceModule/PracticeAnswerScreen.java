package quinzical.view.practiceModule;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import quinzical.view.AnswerScreen;
import quinzical.speech.SoundAdjuster;

/**
 * This class represents the screen that gets displayed when the user is trying to answer the question.
 * 
 * @author Sherman Chin, Dave Shin
 *
 */
public class PracticeAnswerScreen extends AnswerScreen {
	
	private String _clue;
	private String[] _answers;
	private int _remainingAttempts;
	
	private Text _hint;
	private Text _wrongText;
	private Text _attemptsCountText;
	
	public PracticeAnswerScreen(BorderPane pane, String clue, String[] answers) {
		super(pane, clue);
		
		_clue = clue;
		_answers = answers;
		_remainingAttempts = 3; // The user is allowed three attempts at one question.
		
		_hint = new Text("Hint: The first letter of the answer is \"" + _answers[0].charAt(0) + "\"");
		_hint.getStyleClass().addAll("normal-text", "invisible-component");
		
		_wrongText = new Text("Incorrect!");
		_wrongText.getStyleClass().addAll("header-msg", "invisible-component");
		
		_attemptsCountText = new Text("Number of attempts remaining: " + Integer.toString(_remainingAttempts));
		_attemptsCountText.getStyleClass().add("normal-text");
	}
	
	/**
	 * Create GUI components for practice answer screen.
	 */
	protected void createGUI() {
		
		Text instruction = new Text("Clue: " + _clue);
		instruction.getStyleClass().add("information-text");
		instruction.setWrappingWidth(600);
		instruction.setTextAlignment(TextAlignment.CENTER);
		
		Text multipleAnsInstruction = new Text("For clues that have multiple answers, separate them using \",\"");
		multipleAnsInstruction.getStyleClass().add("normal-text");
		
		HBox inputAndSoundBtn = new HBox();
		inputAndSoundBtn.getStyleClass().add("center-screen-box");
		inputAndSoundBtn.getChildren().addAll(_attemptInput, _repeatBtn);
		
		_centerBox.getChildren().addAll(_wrongText, instruction, inputAndSoundBtn, _submitBtn, multipleAnsInstruction,_attemptsCountText, _hint);
		_pane.setCenter(_centerBox);
	}
	
	/**
	 * Adds listeners to buttons
	 */
	protected void handleEvents() {
		super.handleEvents();
		_submitBtn.setOnAction(new EventHandler<ActionEvent>() {	
			@Override
			public void handle(ActionEvent arg0) {
				checkAnswerAndDisplayNext();
			}
		});
		
		_attemptInput.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					checkAnswerAndDisplayNext();
				}
			}
		});
	}
	
	/**
	 * This method checks the user's answers and change the GUI screen accordingly
	 * - Display "Incorrect" to user if answer is false
	 * - Display hint on third attempt
	 * - Change screen to PracticeSoluctionScreen after third attempt
	 */
	private void checkAnswerAndDisplayNext() {
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
			
			//For clues that have multiple answers separated by ","
			if (solution.contains(",")) {
				String[] solutions = solution.split(",");
				String[] attemptAns = attempt.split(",");
				if (solutions.length == attemptAns.length) {
					int count = 0;
					for (int i = 0; i < solutions.length; i++) {
						if (solutions[i].trim().equals(attemptAns[i].trim())) {
							count++;
							System.out.println(count);
						}
					}
					System.out.println(count);
					if (count == solutions.length) {
						correct = true;
						solScrn.displayCorrect();
					}
				}
			} else if (attempt.equals(solution)) {
				solScrn.displayCorrect();
				correct = true;
			}
		}
		
		if (!correct) {
			_remainingAttempts--;
			
			// Only add wrongText when two attempts remain to prevent from duplicate 
			// children from being added.
			
			SoundAdjuster adjuster = new SoundAdjuster("Incorrect", false);

			if (_remainingAttempts == 2) {
				_wrongText.getStyleClass().remove("invisible-component");
				adjuster.speak(adjuster.getText());
			}

			
			if (_remainingAttempts == 1) {
				_hint.getStyleClass().remove("invisible-component");
				adjuster.speak("The first letter of the answer is " + _answers[0].charAt(0));
			} else if (_remainingAttempts < 1) {
				solScrn.displayIncorrect();
			}

			_attemptsCountText.setText("Number of attempts remaining: " + (_remainingAttempts));
		}
	}
}
