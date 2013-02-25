package gmerg.entities.submission.ish;

import java.util.Date;
public class ISHBatch {
    private String batchID;
    private boolean selected;
    private String lastModifiedDate;
    private String lastModifiedBy;
    private Date lastModified;
    private String status;
    
    public ISHBatch() {
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
	
	public Date getLastModified() {
		return lastModified;
	}
	
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
