package gmerg.entities.submission.ish;

public class ISHBatchSubmission {
    private String submissionID;
    private String batchID;
    private boolean selected;
    private boolean locked;
    private String lockedBy;
    private int lockedByPrivilege;
    private String lastModifiedDate;
    private String lastModifiedBy;
    private int dbStatus;
    
    
    public ISHBatchSubmission() {
    }
    
    
    public String getSubmissionID() {
		return submissionID;
	}

	public void setSubmissionID(String submissionID) {
		this.submissionID = submissionID;
	}

	public boolean isSelected() {
    	return this.selected;
    }
    
    public void setSelected(boolean selected) {
    	this.selected = selected;
    }

	public String getBatchID() {
		return batchID;
	}

	public void setBatchID(String batchID) {
		this.batchID = batchID;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	public int getLockedByPrivilege() {
		return lockedByPrivilege;
	}

	public void setLockedByPrivilege(int lockedByPrivilege) {
		this.lockedByPrivilege = lockedByPrivilege;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	// xingjun - 16/10/2009
	public int getDbStatus() {
		return this.dbStatus;
	}
	
	public void setDbStatus(int dbStatus) {
		this.dbStatus = dbStatus;
	}
}
