package gmerg.entities.summary;

import java.util.ArrayList;

public class LabSummary {

    private String labName;
    private String labId;
    private String latestEntryDate;
    private ArrayList summaryResults;
    private boolean viewableByExaminer;// xingjun - 25/11/2009

    public String getLabName() {
        return labName;
    }

    public void setLabName(String value) {
        labName = value;
    }

    public String getLabId() {
        return labId;
    }

    public void setLabId(String value) {
        labId = value;
    }

    public String getLatestEntryDate() {
        return latestEntryDate;
    }

    public void setLatestEntryDate(String value) {
        latestEntryDate = value;
    }

    public ArrayList getSummaryResults() {
        return summaryResults;
    }

    public void setSummaryResults(ArrayList values) {
        summaryResults = values;
    }
    
    public boolean isViewableByExaminer() {
    	return viewableByExaminer;
    }
    
    public void setViewableByExaminer(boolean viewableByExaminer) {
    	this.viewableByExaminer = viewableByExaminer;
    }
}
