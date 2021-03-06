package quinzical.view.gamesModule;

import javafx.scene.control.Button;

/**
 * This class fires an event that is fired when the _dontKnowButton is pressed when the time limit is 
 * reached (timer task is finished).
 * 
 * @author Dave Shin
 *
 */
public class TimerTaskCompletedPaper implements Runnable {
	private Button _dontKnowBtn;
	
	public TimerTaskCompletedPaper(Button dontKnowBtn) {
		_dontKnowBtn = dontKnowBtn;
	}

	@Override
	public void run() {
		_dontKnowBtn.fire(); // If the time limit is reached, nothing gets 
	  	 					 // submitted. This is equivalent to the _dontKnowBtn
		 					 // getting pressed.
	}
}
