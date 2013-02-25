/**
 * 
 */
package gmerg.entities.submission.ish;

/**
 * A composite object returned for displaying the browse all page
 * @author xingjun
 *
 */
public class ISHBrowseSubmissionData {
	
	private ISHBrowseSubmission[] browseSubmissionData;
	private String[][] allColumnTotalNumbers;
	
	// constructor
	public ISHBrowseSubmissionData() {
		
	}
	
	// access method
	public ISHBrowseSubmission[] getBroseSubmissionData() {
		return browseSubmissionData;
	}
	
	public void setBroseSubmissionData(ISHBrowseSubmission[] broseSubmissionData) {
		this.browseSubmissionData = broseSubmissionData;
	}
	
	public String[][] getAllColumnTotalNumbers() {
		return allColumnTotalNumbers;
	}
	
	public void setAllColumnTotalNumbers(String[][] allColumnTotalNumbers) {
		this.allColumnTotalNumbers = allColumnTotalNumbers;
	}
}
