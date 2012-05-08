package gmerg.db;

import java.util.ArrayList;

public interface FocusStageDAO {
	
	public ArrayList[][] getStageList(String[] stage);
	public String[][] getStageList(String[] stage, String organ);
	// different version of last one
	public String[] getStageList(String assayType, String[] stage, String organ, String symbol);
	public Object[][] getGeneIndex(String prefix, String organ);
	public String getDpcStageValue(String theilerStage);
}
