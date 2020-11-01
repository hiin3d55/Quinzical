package quinzical.speech;

import java.io.IOException;
import java.util.stream.Stream;

public class SpeechSynthesisThread extends Thread {
	private Process _process;
	
	@Override
	public void run() {
		// Bash command for speaking out the clue.
		String speakClueCmd = "festival -b clueFile.scm";
				
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", speakClueCmd);
		try {	
			_process = builder.start();
			try {
				_process.waitFor(); 
			} catch (InterruptedException ie) {
				System.out.println("Error with waiting for the speech synthesis process to finish.");
				ie.printStackTrace();	
			}
		} catch (IOException e) {
			System.out.println("Error with using festival to read out the question.");
			e.printStackTrace();
		}
	}
}
