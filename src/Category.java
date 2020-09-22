import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Category {
	private Clue _clue;
	private File _categoryRecordFolder;

	public Category() {
		_clue = new Clue();
		_categoryRecordFolder = new File("data/");
	}

	public String[] getCategories(boolean gameModule) {
		File file = new File("questionBank");
		String[] allCategories = file.list();

		if (!gameModule) {
			return allCategories;
		}
			
		if (_categoryRecordFolder.exists()) {
			return _categoryRecordFolder.list();
		} else {
			_categoryRecordFolder.mkdir();
			String[] categories = randomiseCategories(allCategories);
			
			try {
				recordCategories(categories);
			} catch (IOException e) {
				e.printStackTrace();
			}
			_clue.recordClues(categories);
			
			return categories;
		}
		

		
	}
	
	private void recordCategories(String[] categories) throws IOException {
		for (int i = 0; i < categories.length; i++) {
			File file = new File(_categoryRecordFolder.getPath() + "/" + categories[i]);
			file.createNewFile();
		}
	}

	private String[] randomiseCategories(String[] categories) {
		Set<String> randomed = new HashSet<String>();

		while (randomed.size() < 5) {
			randomed.add(categories[(int)(Math.random()*categories.length)]);
		}
		return randomed.toArray(new String[randomed.size()]);
	}
	
	public String[] requestClue(String category, boolean gameModule) {
		return _clue.getClues(category, gameModule);
	}
	
	public String getClueAnswer(String category, String clue) {
		return _clue.getAnswer(category, clue);
	}
}
