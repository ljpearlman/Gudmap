/**
 * 
 */
package gmerg.entities.submission;

/**
 * @author xingjun
 *
 */
public class LockingInfo {
	
	private String submissionId;
	private String lockedBy;
	private int lockTime; // in minute
	
	
	public String getSubmissionId() {
		return this.submissionId;
	}
	
	public void setsubmissionId(String submissionId) {
		this.submissionId = submissionId;
	}
	
	public String getLockedBy() {
		return this.lockedBy;
	}
	
	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}
	
	public int getLockTime() {
		return this.lockTime;
	}
	
	public void setLockTime(int lockTime) {
		this.lockTime = lockTime;
	}

}
