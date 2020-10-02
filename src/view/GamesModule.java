package view;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.QuestionBank;

/**
 * This class represents the screen of Games Module. It displays the question board and the user is able to choose the
 * question that they want to answer.
 * 
 * @author Dave Shin
 *
 */
public class GamesModule {
	private Stage _primaryStage;
	
	private QuestionBank _questionBank;
	private ArrayList<ArrayList<Question>> _allQuestions; // Keeps track of which clues have been revealed.
	
	public GamesModule(Stage primaryStage) {
		_primaryStage = primaryStage;
		
		// QuestionBank retrieves the data from the backend. The argument is true because we are in Games Module.
		_questionBank = new QuestionBank(true);
		_allQuestions = new ArrayList<ArrayList<Question>>();
	}
	
	/**
	 * This method is only called once until the game is reset. This is because this method initialises the GamesModule.
	 */
	private void initialise() {
		BorderPane gamesModPane = new BorderPane();
		gamesModPane.getStyleClass().add("background-screen");

		VBox gamesModBox = new VBox();
		gamesModBox.getStyleClass().add("center-screen-box");

		Text instruction1 = new Text("Please choose a clue to be read out");
		instruction1.getStyleClass().add("normal-text");
		Text instruction2 = new Text("You can only choose the lowest money value for each category.");
		instruction2.getStyleClass().add("normal-text");

		gamesModBox.getChildren().addAll(instruction1, instruction2);
		
		String[] categoriesStrArray = _questionBank.requestCategory();
		
		HBox clueGrid = new HBox();
		clueGrid.getStyleClass().addAll("center-screen-box", "clue-grid");

		// Set out the GUI for Games Module. The end product is a screen with multiple buttons for the clues.
		for (String categoryStr : categoriesStrArray) {
			VBox categoryColumn = new VBox();
			categoryColumn.getStyleClass().addAll("center-screen-box");
			
			Text categoryText = new Text(categoryStr);
			categoryText.getStyleClass().add("normal-text");
			categoryColumn.getChildren().add(categoryText);
			
			String[] clues = _questionBank.requestClueForCategory(categoryStr);
			
			ArrayList<Question> questions = new ArrayList<Question>();
			
			
			// Create the buttons for the clues.
			int valueIdx = 0;
			for (String clue : clues) {
				int amount = _questionBank.getValues(categoryStr)[valueIdx];
						
				Question question = new Question(amount, clue, categoryStr, 
						_questionBank.answerForClue(categoryStr, clue));
				
				questions.add(question);

				
				Button clueButton = question.getButton();
				
				if (valueIdx == 0) {
					clueButton.getStyleClass().add("golden-button");
				} else {
					clueButton.getStyleClass().add("normal-button");
				}
				categoryColumn.getChildren().add(question.getButton());
				valueIdx++;
			}
			
			_allQuestions.add(questions);
			
			valueIdx = 0;
			clueGrid.getChildren().add(categoryColumn);
		}
		
		gamesModBox.getChildren().add(clueGrid);
		gamesModPane.setCenter(gamesModBox);
		
		Scene gamesModScene = new Scene(gamesModPane, 600, 400);
		gamesModScene.getStylesheets().add("view/application.css");

		_primaryStage.setScene(gamesModScene);
		_primaryStage.show();
	}
	
	public void display() {
		
		// When there are no clues left, treat the user to the Reward Screen.
		if (_questionBank.requestCategory().length == 0) {
			RewardScreen rewardScrn = new RewardScreen(_primaryStage);
			rewardScrn.display();
		} else { // Keep displaying the GamesModule if there are still clues remaining.
			initialise();
			handleEvents();
		}
	}
	
	public void handleEvents() {
		
		// When a clue button is pressed.
		for (ArrayList<Question> questionsForCategory : _allQuestions) {
			for (Question pressedQuestion : questionsForCategory) {
				pressedQuestion.getButton().setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {	
						
						// We need to check if the clue that is pressed has the minimum value for that category because
						// only the clues that have the minimum value in their category can be selected.
						if (questionsForCategory.indexOf(pressedQuestion) == 0) {
							
							// Do operations that removes the pressed clue so that it is removed from the Games Module.
							_allQuestions.get(_allQuestions.indexOf(questionsForCategory)).remove(pressedQuestion);
							_questionBank.updateClue(pressedQuestion.getCategory(), pressedQuestion.getClue());
							
							// If a category contains no clues, remove that category from _allQuestions.
							if (_allQuestions.get(_allQuestions.indexOf(questionsForCategory)).isEmpty()) {
								_allQuestions.remove(questionsForCategory);
							}
							
							AnswerScreen answerScrn = new AnswerScreen(_primaryStage, pressedQuestion);
							answerScrn.display();
						}
					}
				});
			}
		}
	}
}
