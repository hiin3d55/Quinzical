package quinzical.view.gamesModule;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import quinzical.model.User;
import quinzical.model.Users;
import quinzical.model.QuestionBank;
import quinzical.model.Score;
import quinzical.view.WelcomeScreen;

/**
 * This class represents the reward screen which is a screen where the reward is
 * displayed. This screen gets displayed when the user have answered all
 * available question. On this screen, the user can view their score and record
 * their details to the leader board. Upon saving, the user's score is reset to
 * zero.
 * 
 * @author Dave Shin
 *
 */
public class RewardScreen {

	// Amount of score that is needed to win a specific medal.
	private static final int bronze = 3000;
	private static final int silver = 6000;

	private BorderPane _pane;
	private Button _saveGameBtn;
	private Button _playAgnBtn;
	private Text _invalidInput;
	private TextField _userName;
	private TextField _userId;
	private VBox _rewardBox;

	private Score _score;
	private QuestionBank _questionBank;
	private Users _users;

	public RewardScreen(BorderPane pane) {
		_pane = pane;

		_rewardBox = new VBox();

		_saveGameBtn = new Button("Save");
		_saveGameBtn.getStyleClass().add("golden-button");

		_playAgnBtn = new Button("Play Again");
		_playAgnBtn.getStyleClass().add("golden-button");

		_userName = new TextField();
		_userName.setPromptText("User Name");
		_userId = new TextField();
		_userId.setPromptText("User ID");

		_score = new Score();
		_questionBank = new QuestionBank(true);
		_users = new Users();

		_invalidInput = new Text();
		_invalidInput.getStyleClass().addAll("normal-text", "invinsible-component");
	}

	/**
	 * Displays the reward screen.
	 */
	public void display() {
		handleEvents();

		_rewardBox.getStyleClass().add("center-screen-box");

		Text congratulationMsg = new Text("Congratulations! All Questions Attempted!");
		congratulationMsg.getStyleClass().add("header-msg");

		Text infoMsg = new Text("Your final score is");
		infoMsg.getStyleClass().add("normal-text");
		infoMsg.setStyle("-fx-fill: #EAEAEA;");

		Text scoreText = new Text(Integer.toString(_score.getScore()));
		scoreText.getStyleClass().addAll("information-text");
		scoreText.setStyle("-fx-fill: #E0FFFF;");

		Text saveScoreText = new Text("Save your score under: ");
		saveScoreText.getStyleClass().add("information-text");

		HBox userInputBox = new HBox();
		userInputBox.getStyleClass().add("center-screen-box");
		userInputBox.getChildren().addAll(_userName, _userId);

		_rewardBox.getChildren().addAll(congratulationMsg, infoMsg, scoreText, saveScoreText, userInputBox,
				_saveGameBtn, _invalidInput);
		_pane.setCenter(_rewardBox);
		_pane.getBottom().getStyleClass().removeAll("invisible-component");
	}

	/**
	 * Displays the GUI that tells the user their details have been saved and their
	 * ranking.
	 */
	private void displaySaved() {
		VBox userSavedBox = new VBox();
		userSavedBox.getStyleClass().add("center-screen-box");
		
		Text header = new Text("You are ranked");
		header.getStyleClass().add("information-text");

		Text userRanking = new Text("Number " + _users.getRanking(_userId.getText()) + "!");
		userRanking.getStyleClass().add("header-msg");


		Text winningText = new Text("And have won a");
		winningText.getStyleClass().add("normal-text");
		
		userSavedBox.getChildren().addAll(header, userRanking, winningText);

		displayMedal(userSavedBox);
		
		userSavedBox.getChildren().add(_playAgnBtn);
		
		VBox.setMargin(userRanking, new Insets(10,0,10,0));
		VBox.setMargin(_playAgnBtn, new Insets(10, 0, 0, 0));
		
		_pane.setCenter(userSavedBox);
		_pane.getBottom().getStyleClass().add("invisible-component");
	}

	/**
	 * Display a bronze/silver/gold medal to the users depending on their scores.
	 * @param centerBox The container to be added to the center of the layout.
	 */
	private void displayMedal(VBox centerBox) {
		if (_score.getScore() <= bronze) {
			addMedal(centerBox, "bronze");
		} else if (_score.getScore() > bronze && _score.getScore() <= silver) {
			addMedal(centerBox, "silver");
		} else {
			addMedal(centerBox, "gold");
		}
	}

	
	/**
	 * Adds a medal image to the screen. If the image file can't be found, then display a text instead.
	 * @param centerBox The container where the medal/text should be added into
	 * @param medalType A String which is either "bronze", "silver", or "gold"
	 */
	private void addMedal(VBox centerBox, String medalType) {
		Image image;
		try {
			image = new Image(new FileInputStream("resources/" + medalType + "-medal.png"));
			ImageView imageView = new ImageView();
			imageView.setImage(image);

			imageView.setFitHeight(100);
			imageView.setFitWidth(100);

			imageView.setPreserveRatio(true);						

			centerBox.getChildren().add(imageView);
			
		} catch (FileNotFoundException e) {
			//If image not found, then simply display a text
			Text medalText = new Text(medalType + " medal!");

			medalText.getStyleClass().add("information-text");			
			medalText.setWrappingWidth(600);
			medalText.setTextAlignment(TextAlignment.CENTER);

			centerBox.getChildren().add(medalText);
		}
	}

	public void handleEvents() {
		_saveGameBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				checkUserDetails();
			}
		});

		_userId.setOnKeyPressed(new UserTextFieldListener());

		_userName.setOnKeyPressed(new UserTextFieldListener());

		_playAgnBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_score.resetScore();
				_questionBank.resetGame();
				
				// Updates the score
				Text scoreText = (Text) _pane.getTop();
				_score = new Score();
				scoreText.setText("Current Score: " + _score.getScore());

				WelcomeScreen welcomeScrn = new WelcomeScreen(_pane);
				welcomeScrn.display();
			}
		});
	}

	/**
	 * This listener checks if an ENTER key is pressed for a textfield.
	 * 
	 * @author Sherman
	 *
	 */
	private class UserTextFieldListener implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent event) {
			if (event.getCode().equals(KeyCode.ENTER)) {
				checkUserDetails();
			}
		}

	}

	/**
	 * This method checks if the user details given are valid E.g. The user name and
	 * user ID can not be empty. User ID must also not have been existed before.
	 */
	private void checkUserDetails() {
		if (_userName.getText().equals("") || _userId.getText().equals("")) {
			_invalidInput.setText("Please fill in all required fields");
			_invalidInput.getStyleClass().remove("invinsible-component");

		} else if (_users.userIdExists(_userId.getText())) {
			_invalidInput.setText("User ID already exists. Please enter another user ID");
			_invalidInput.getStyleClass().remove("invinsible-component");

		} else {
			User user = new User(_userName.getText(), _userId.getText(), Integer.toString(_score.getScore()));
			_users.addUser(user);

			displaySaved();
		}
	}
}
