/**
 * 
 */
package gmerg.entities.submission;

/**
 * @author xingjun
 *
 */
public class StatusNote {
	
	private int statusNoteId;
	private String submissionId;
	private String statusNote;
	private boolean selected;
	
	public StatusNote() {
		
	}
	
	public int getStatusNoteId() {
		return statusNoteId;
	}
	
	public void setStatusNoteId(int statusNoteId) {
		this.statusNoteId = statusNoteId;
	}

	public String getSubmissionId() {
		return submissionId;
	}
	
	public void setSubmissionId(String submissionId) {
		this.submissionId = submissionId;
	}

	public String getStatusNote() {
		return statusNote;
	}
	
	public void setStatusNote(String statusNote) {
		this.statusNote = statusNote;
	}

	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
