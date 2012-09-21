/**
 * 
 */
package gmerg.beans;

import java.util.Map;

import javax.faces.context.FacesContext;

import gmerg.assemblers.EditSubmissionAssembler;
import gmerg.assemblers.ISHSubmissionAssembler;
import gmerg.entities.submission.StatusNote;
import gmerg.utils.FacesUtil;

/**
 * @author xingjun
 *
 */
public class EditSubmissionBean {
	
	private ISHSubmissionAssembler ishSubmissionAssembler;
	private EditSubmissionAssembler editSubmissionAssembler;
	private StatusNote[] statusNotes;
	private boolean noStatusNoteCheckedForDeletion;
	private boolean emptyStatusNoteExists;
	private String accessionId;
	private String userName;
	
	
	public EditSubmissionBean() {
//		System.out.println("EditSubmissionBean initialised########");
		
    	// get user name
        Object object = FacesUtil.getSessionValue("UserBean");
        if (object != null) {
        	UserBean ub = (UserBean)object;
        	this.userName = ub.getUserName();
        } else {
        	this.userName = null;
        }
    	
		ishSubmissionAssembler = new ISHSubmissionAssembler();
        // try to get accesion id from id parameter - first entered the submission detail page
		FacesContext context = FacesContext.getCurrentInstance();
        Map requestParams =
            context.getExternalContext().getRequestParameterMap();
        String accessionId = (String)requestParams.get("id");
//        System.out.println("accession id: " + accessionId);
        
        // try to get accession id from submissionId parameter - return to submission detail page
        // after modifying status note
        if (accessionId == null || accessionId.equals("")) {
            accessionId = FacesUtil.getRequestParamValue("submissionId");
        }
        
        if (accessionId != null && !accessionId.equals("")) {
        	this.setAccessionId(accessionId);
        	this.statusNotes = ishSubmissionAssembler.getStatusNotes(accessionId);
        }
        
//        System.out.println("statusNotes size: " + this.statusNotes.length);
//        System.out.println("statusNotes: " + this.statusNotes[0]);
	}
	
	
	public StatusNote[] getStatusNotes() {
//		System.out.println("EditSubmissionBean:getStatusNotes method#######");
		return this.statusNotes;
	}
	
	public void setStatusNotes(StatusNote[] statusNotes) {
		this.statusNotes = statusNotes;
	}
	
	public boolean getNoStatusNoteCheckedForDeletion() {
		return this.noStatusNoteCheckedForDeletion;
	}
	
	public void setNoStatusNoteCheckedForDeletion(boolean noStatusNoteCheckedForDeletion) {
		this.noStatusNoteCheckedForDeletion = noStatusNoteCheckedForDeletion;
	}
	
	public boolean isEmptyStatusNoteExists() {
		return this.emptyStatusNoteExists;
	}
	
	public void setEmptyStatusNoteExists(boolean emptyStatusNoteExists) {
		this.emptyStatusNoteExists = emptyStatusNoteExists;
	}
	
	public String getAccessionId() {
		return this.accessionId;
	}
	
	public void setAccessionId(String accessionId) {
		this.accessionId = accessionId;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String deleteStatusNote() {
		
//		System.out.println("EditSubmissionBean:deleteStatusNote method#######");
		// cannot delete if there's no status note
		if (this.statusNotes == null) {
			this.noStatusNoteCheckedForDeletion = false;
		} else {
			// check how many status notes have been checked
			int numberOfCheckedStatusNote =
				this.getNumberOfStatusNoteMarkedForDeletion(this.statusNotes);
			if (numberOfCheckedStatusNote < 1) { // no entry been checked
				this.noStatusNoteCheckedForDeletion = true;
			} else { // restructure the status notes
				StatusNote[] currentStatusNotes = this.statusNotes;
				int len = currentStatusNotes.length;
				if (len == numberOfCheckedStatusNote) { // delete all status notes
					System.out.println("delete all status notes!");
					this.statusNotes = null;
					this.noStatusNoteCheckedForDeletion = false;
					
					// set session value: adding status note
					FacesUtil.setSessionValue("addingStatusNote", "false");
					
				} else { // delete part of status notes
					System.out.println("delete part of status notes!");
					int newLen = len - numberOfCheckedStatusNote;
					StatusNote[] newStatusNotes = new StatusNote[newLen];
					for (int i=0,j=0;i<len;i++) {
						if (!this.statusNotes[i].isSelected()) {
							newStatusNotes[j] = this.statusNotes[i];
							j++;
						}
					}
					this.statusNotes = newStatusNotes;
					this.noStatusNoteCheckedForDeletion = false;
					
					// if the added status note has been checked, delete it and allow user to add new status note
					String statusNoteValue = statusNotes[newLen-1].getStatusNote();
					if (statusNoteValue != null && !statusNoteValue.equals("")) {
//            			System.out.println("newLen: " + newLen);
//            			System.out.println("pattern: " + patterns[newLen - 1].getPattern());
						FacesUtil.setSessionValue("addingStatusNote", "false");
					}
				}
			} // end of restructure the status note
		} // end of checking status note existing
//		System.out.println("no checked: " + this.noStatusNoteCheckedForDeletion);
		this.emptyStatusNoteExists = false;
		return "editStatusNote";
	}
	
	/**
	 * 
	 * @return
	 */
	public String addStatusNote() {
		
//		System.out.println("EditSubmissionBean:addStatusNote method#######");

		// only execute when there's no patter is being added
		String addingStatusNote = (String)FacesUtil.getSessionValue("addingStatusNote");
		System.out.println("addStatusNote:addingStatusNote: " + addingStatusNote);
		if (addingStatusNote == null || addingStatusNote.equals("false")) { // have not added note yet
			if (this.statusNotes == null) { // there's no status note yet; need to create one
				statusNotes = new StatusNote[1];
				statusNotes[0] = new StatusNote();
//				statusNotes[0].setStatusNote("");
			} else { // add one extra status note entry into current status note array
				StatusNote[] currentStatusNotes = statusNotes;
				int len = currentStatusNotes.length;
				StatusNote[] newStatusNotes = new StatusNote[len+1];
				for (int i=0;i<len;i++) {
					newStatusNotes[i] = currentStatusNotes[i];
				}
				newStatusNotes[len] = new StatusNote();
				this.statusNotes = newStatusNotes;
				
				this.noStatusNoteCheckedForDeletion = false;
				
				// set session value: adding status note
				FacesUtil.setSessionValue("addingStatusNote", "true");
			}
		} else { // added status note and have not submitted yet
			// do nothing except resetting parameters
			this.emptyStatusNoteExists = false;
			this.noStatusNoteCheckedForDeletion = false;
		}
		return "editStatusNote";
	}
	
	/**
	 * 
	 * @return
	 */
	public String cancelModification() {
		
//		System.out.println("EditSubmissionBean:cancelModification method#######");
		// get status notes
//        String accessionId = FacesUtil.getRequestParamValue("submissionId");
//        System.out.println("accession id: " + accessionId);
        
//        if (accessionId != null && !accessionId.equals("")) {
//        	this.statusNotes = ishSubmissionAssembler.getStatusNotes(this.accessionId);
//        }
    	this.statusNotes = ishSubmissionAssembler.getStatusNotes(this.accessionId);
        
        // reset
        FacesUtil.setSessionValue("addingStatusNote", "false");
        this.noStatusNoteCheckedForDeletion = false;
		this.emptyStatusNoteExists = false;
        
		return "editSubmission";
	}
	
	public String commitModification() {
//		System.out.println("EditSubmissionBean:commitModification method#######");
		
		// cannot edit if not logged in
    	if (this.userName == null) {
        	return "editStatusNote";
    	}
    	
    	// empty notes are not allowed
    	if (this.emptyStatusNotesExists(this.statusNotes)) {
    		System.out.println("emptyStatusNotesExists!!!!!!!");
    		// give the message to ask user to input status note
    		this.emptyStatusNoteExists = true;
        	return "editStatusNote";
    	}
    	
		boolean statusNotesUpdated = false;
		this.editSubmissionAssembler = new EditSubmissionAssembler();
    	
    	// get status note detail from database
    	StatusNote[] statusNotesInDB = ishSubmissionAssembler.getStatusNotes(this.accessionId);
    	
    	if (statusNotesInDB == null) { // there's no corresponding status note in db
    		if (this.statusNotes != null) { // has status notes on page, insert status notes into DB
    			statusNotesUpdated = 
    				editSubmissionAssembler.addStatusNotes(this.accessionId, statusNotes, userName);
    			if (!statusNotesUpdated) { // failed to add status notes, reset
    				this.statusNotes = statusNotesInDB;
    			}
    		}
    	} else { // corresponding status notes existing
    		if (this.statusNotes != null) { // has status notes on page, update status notes in DB
    			statusNotesUpdated = editSubmissionAssembler.updateStatusNotes(this.accessionId,
    					statusNotesInDB, this.statusNotes, userName);
    		} else { // no status notes on page, delete from DB
    			statusNotesUpdated =
    				editSubmissionAssembler.deleteAllStatusNotes(this.accessionId, userName);
    			if (!statusNotesUpdated) { // failed to delete status notes, reset
    				this.statusNotes = statusNotesInDB;
    			}
    		}
    	}
        // reset system parameters
        FacesUtil.setSessionValue("addingStatusNote", "false");
        this.noStatusNoteCheckedForDeletion = false;
		this.emptyStatusNoteExists = false;
        
    	return "editStatusNote";
	}
	
	private boolean emptyStatusNotesExists(StatusNote[] statusNotes) {
		int len = statusNotes.length;
		for (int i=0;i<len;i++) {
			if (statusNotes[i].getStatusNote() == null
					|| statusNotes[i].getStatusNote().trim().equals("")){
				return true;
			}
		}
		return false;
	}

	
    /**
     * 
     * @param statusNotes
     * @return
     */
	private int getNumberOfStatusNoteMarkedForDeletion(StatusNote[] statusNotes) {
    	int count = 0;
    	for (int i=0;i<statusNotes.length;i++) {
    		if (statusNotes[i].isSelected()) {
    			count++;
    		}
    	}
    	return count;
    }

}
