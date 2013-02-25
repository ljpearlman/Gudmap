/**
 * 
 */
package gmerg.entities.submission;

/**
 * @author xingjun
 * submissionType: 1, the submission object contains ish submission
 *                 2, the submission object contains microarray submission
 */
public class SubmissionData {
	
	private Object submission;
	private int submissionType;
	
	public SubmissionData() {
		
	}
	
	public Object getSubmission() {
		return submission;
	}
	
	public void setSubmission(Object submission) {
		this.submission = submission;
	}
	
	public int getSubmissionType() {
		return submissionType;
	}
	
	public void setSubmissionType(int submissionType) {
		this.submissionType = submissionType;
	}

}
