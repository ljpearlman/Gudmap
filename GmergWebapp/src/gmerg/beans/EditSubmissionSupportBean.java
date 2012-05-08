/**
 * 
 */
package gmerg.beans;

import java.util.Map;

import javax.faces.context.FacesContext;

import gmerg.assemblers.ISHSubmissionAssembler;
import gmerg.assemblers.EditExpressionAssembler;
import gmerg.entities.submission.StatusNote;
import gmerg.entities.submission.LockingInfo;
import gmerg.utils.FacesUtil;


/**
 * @author xingjun
 *
 */
public class EditSubmissionSupportBean {
	
	private StatusNote[] statusNotes;
	private ISHSubmissionAssembler iSHSubmissionAssembler;
	private EditExpressionAssembler editExpressionAssembler;
//	private LockingInfo lockingInfo;
	private String subAccessionId;
	private String emptyString = "";
	
	/**
	 * constructor
	 * 
	 * First check if submission id is passed in
	 *   - if the page is loaded by clicking on links in other pages or by 
	 *     clicking on command link(s) on submission detailed page
	 *   
	 */
	public EditSubmissionSupportBean() {
		
//		System.out.println("EditSubmissionSupportBean initialised########");

		EditSubmissionBean editSubmissionBean = null;
        String accessionIdOfSession = null;
        
		// get parameters
        // try to get accesion id from id parameter - first entered the submission detail page
		FacesContext context = FacesContext.getCurrentInstance();
        Map requestParams =
            context.getExternalContext().getRequestParameterMap();
        subAccessionId = (String)requestParams.get("id");
//        System.out.println("accession id: " + subAccessionId);
        
        // try to get accession id from submissionId parameter - return to submission detail page
        // after modifying status note
        if (subAccessionId == null || subAccessionId.equals("")) {
//        	System.out.println("try to get subAccessionId from submissionId");
            subAccessionId = FacesUtil.getRequestParamValue("submissionId");
        }
		
//		Object object = FacesUtil.getSessionValue("editSubmissionBean");
        Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("editSubmissionBean");
		if (object != null) {
//			System.out.println("got the editSubmissionBean!");
			editSubmissionBean = (EditSubmissionBean)object;
			accessionIdOfSession = editSubmissionBean.getAccessionId();
		}
		
		// if the submission id is not passed in, go to EditSubmissionBean to get the id
		if (subAccessionId == null || subAccessionId .equals("")) {
//			System.out.println("try to get accessionId from editSubmissionBean!");
			subAccessionId  = accessionIdOfSession;
		}
		
		// get status note detail
		iSHSubmissionAssembler = new ISHSubmissionAssembler();
		this.statusNotes = iSHSubmissionAssembler.getStatusNotes(subAccessionId);
		
		// get locking info
//		this.editExpressionAssembler = new EditExpressionAssembler();
//		lockingInfo = editExpressionAssembler.getLockingInfo(subAccessionId);
		
		// compare 2 sets of ids only if EditSubmissionBean has been initialised
//		System.out.println("editSubmissionSupportBean:subAccessionId: " + subAccessionId);
//		System.out.println("editSubmissionBean:accessionId: " + accessionIdOfSession);
		if (accessionIdOfSession != null) {
			// user has been to the other page, need to reset current editSubmissionBean
			if (!accessionIdOfSession.equals(subAccessionId)) {
//				System.out.println("set editSubmissionBean!");
				editSubmissionBean.setAccessionId(subAccessionId);
				editSubmissionBean.setStatusNotes(this.statusNotes);

		        // reset the parameters 
				// when user diverted to other submissions, the parameters should
				// be reset to their initial values
				FacesUtil.setSessionValue("addingStatusNote", "false");
		        editSubmissionBean.setNoStatusNoteCheckedForDeletion(false);
			}
			// set locking info
		}
	}
	
	public String getSubAccessionId() {
		return this.subAccessionId;
	}
	
	public void setSubAccessionId(String subAccessionId) {
		this.subAccessionId = subAccessionId;
	}
	
	public String getEmptyString() {
		return this.emptyString;
	}
	
	public void setEmptyString(String emptyString) {
		this.emptyString = emptyString;
	}
}
