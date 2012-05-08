package gmerg.beans;

import gmerg.assemblers.BatchAnnotationAssembler;
import gmerg.entities.Globals;
import gmerg.entities.submission.ish.ISHBatchSubmission;
import gmerg.utils.CookieOperations;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;

import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;

public class ISHBatchSubmissionBean {
	private ISHBatchSubmission[] submissions;
	private BatchAnnotationAssembler batchAnnotationAssembler;
	private int status;
	private String batchID;
	private String userName;
	//0 normal; 1 no id; 2 only one id is allowed; 3 can't delete locked id
	private boolean batchCompleted;
	
	/**
	 * <p>modified by xingjun - 01/09/2009
	 * - get batch status during the initialisation and the batchStatus value
	 * will be used in the page display</p>
	 *
	 */
	public ISHBatchSubmissionBean() {
        Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("userBean");
        
        int privilege = 0;
        int pi = 0;
        String userName = null;
        UserBean ub = null;
        if (object != null) {
        	ub = (UserBean)object;
        	if(null != ub.getUser()) {
        		privilege = ub.getUser().getUserPrivilege();
        		pi = ub.getUser().getUserPi();
        		userName = ub.getUserName();
        	}
        }
        batchAnnotationAssembler = new BatchAnnotationAssembler();
        String batch = (String)FacesUtil.getRequestParamValue("batchID");
        if(null != batch && !batch.equals("")) {
        	CookieOperations.replaceCookieValues("BATCHID", new String[]{batch});	
        	setBatchID(batch);
        	
        } else {
        	batch = (String)CookieOperations.getCookieValue("BATCHID");
        }
        if(privilege >= 3) {
        	//if there is no unfinished batch, then create and return a new batch ID        	
        	submissions = batchAnnotationAssembler.getSubmissionByBatchId(batch);// submissions with batch ID
        	
        	// added by xingjun - 01/09/2009 - get batch status - start
        	int batchStatus = batchAnnotationAssembler.getBatchStatus(batch);
//        	System.out.println("ISHBatchSubmissionBean:batch:#" + batch +"#batchStatus: " + batchStatus);
        	if (batchStatus == 3) {
        		this.setBatchCompleted(true);
        	} else {
        		this.setBatchCompleted(false);
        	}
        	// added by xingjun - 01/09/2009 - get batch status - end
        }
               //System.out.println("GET SUBS:"+batch+":"+privilege);
        
	}

	public ISHBatchSubmission[] getSubmissions() {
	//System.out.println("ISHBatchSubmission[] getSubmissions is called");
		return submissions;
	}

	public void setSubmissions(ISHBatchSubmission[] submissions) {
		this.submissions = submissions;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public String unLockSubmission() {
		System.out.println("unLockSub is called");
		Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("userBean");
        int privilege = 0;
        int pi = 0;
        String userName = null;
        UserBean ub = null;
        if (object != null) {
        	ub = (UserBean)object;
        	if(null != ub.getUser()) {
        		privilege = ub.getUser().getUserPrivilege();
        		pi = ub.getUser().getUserPi();
        		userName = ub.getUserName();
        	}
        }
        String batch = (String)CookieOperations.getCookieValue("BATCHID");
        String subId = (String)FacesUtil.getRequestParamValue("subId");
        if(privilege >= 3) {			
            batchAnnotationAssembler.unlockSubmission(subId, ub.getUser());
            submissions = batchAnnotationAssembler.getSubmissionByBatchId(batch);
        }
		return null;
	}
	
	public String deleteSubmission() {
        Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("userBean");
        int privilege = 0;
        int pi = 0;
        String userName = null;
        UserBean ub = null;
        if (object != null) {
        	ub = (UserBean)object;
        	if(null != ub.getUser()) {
        		privilege = ub.getUser().getUserPrivilege();
        		pi = ub.getUser().getUserPi();
        		userName = ub.getUserName();
        	}
        }
        //System.out.println("SUBMISSIONS:"+submissions.length);
        String[] selectedIds = checkSelected(submissions);
        String[] freeIds = null;
        if(null != selectedIds) {
        	freeIds = checkLocked(submissions);
        	if(null == freeIds || freeIds.length < selectedIds.length) {
        		setStatus(3);
        	} else {
        		setStatus(0);        		
        	}
//        	if(null != freeIds) {
//        		for(int i = 0; i < freeIds.length; i++) {
//        			System.out.println("freeIds:" + i + ":" + freeIds[i]);
//        		}
//        	}
//        	for(int i = 0; i < selectedIds.length; i++) {
//        		System.out.println("selectedIds:" + i + ":" + selectedIds[i]);
//        	}
        	
        } else {
        	setStatus(1);
//        	System.out.println("selectedIds is null");
        }
		
		String batch = (String)CookieOperations.getCookieValue("BATCHID");
			
//		System.out.println("PI:"+pi);
		if(privilege >= 3 && null != freeIds) {			
        	////modified by ying 20080909 should be deleteTemporarySubmission(freeIds, ub.getUser())
        	//if(null != batch) {
            batchAnnotationAssembler.deleteTemporarySubmission(selectedIds, ub.getUser());
            submissions = batchAnnotationAssembler.getSubmissionByBatchId(batch);
        	//}
        }		
		return null;
	}
	
	public String[] checkSelected(ISHBatchSubmission[] submissions) {
		ArrayList<ISHBatchSubmission> list = new ArrayList<ISHBatchSubmission>();
		if(null != submissions) {
			for(int i = 0; i < submissions.length; i++) {
				if(submissions[i].isSelected()) {
					list.add(submissions[i]);
				}
			}
		}
		if(list.size() == 0) {
			return null;
		}
		String[] selectedIds = new String[list.size()];
		for(int i = 0; i < list.size(); i++) {
			selectedIds[i] = ((ISHBatchSubmission)list.get(i)).getSubmissionID();
		}
		return selectedIds;
	}
	
	public String[] checkLocked(ISHBatchSubmission[] submissions) {
		ArrayList<ISHBatchSubmission> freeList = new ArrayList<ISHBatchSubmission>();
		if(null != submissions) {
			for(int i = 0; i < submissions.length; i++) {
				if(!submissions[i].isLocked()) {
					freeList.add(submissions[i]);
				}
			}
		}
		if(freeList.size() == 0) {
			return null;
		}
		String[] selectedIds = new String[freeList.size()];
		for(int i = 0; i < freeList.size(); i++) {
			selectedIds[i] = ((ISHBatchSubmission)freeList.get(i)).getSubmissionID();
		}
		return selectedIds;		
	}
	
	public String addSubmission() {
		setStatus(0);
	
        Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("userBean");
        int privilege = 0;
        //int pi = 0;
        String userName = null;
        UserBean ub = null;
        if (object != null) {
        	ub = (UserBean)object;
        	if(null != ub.getUser()) {
        		privilege = ub.getUser().getUserPrivilege();
        		//pi = ub.getUser().getUserPi();
        		userName = ub.getUserName();
        	}
        }
        
		String batch = (String)CookieOperations.getCookieValue("BATCHID");
		String pi = (String)CookieOperations.getCookieValue("LABID");
	        //System.out.println("HEYHEY:"+batch);
        if(privilege >= 3) {       	
        	//if(null != batch) {
            batchAnnotationAssembler.addSubmission(batch, ub.getUser(),pi);
            submissions = batchAnnotationAssembler.getSubmissionByBatchId(batch);
        	//}
        }		
		return null;
	}
	
	public String duplicateSubmission() {
        Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("userBean");
        int privilege = 0;
        int pi = 0;
        String userName = null;
        UserBean ub = null;
        if (object != null) {
        	ub = (UserBean)object;
        	if(null != ub.getUser()) {
        		privilege = ub.getUser().getUserPrivilege();
        		pi = ub.getUser().getUserPi();
        		userName = ub.getUserName();
        	}
        }
        String sourceId = null;
        String[] selectedIds = checkSelected(submissions);
        if(null != selectedIds && selectedIds.length == 1) {
        	sourceId = selectedIds[0];
        } else if(null == selectedIds) {
        	setStatus(1);
        	//"no submission is selected";
        } else if(selectedIds.length > 1) {
        	setStatus(2);
        	//set error message "only one submissison should be selected"
        }
        
		String batch = (String)CookieOperations.getCookieValue("BATCHID");
	        
        if(privilege >= 3 && null != sourceId) {
        	
        	//if(null != batch) {
            batchAnnotationAssembler.duplicateSubmission(sourceId, ub.getUser());
            submissions = batchAnnotationAssembler.getSubmissionByBatchId(batch);
        	//}
        }		
		return null;
	}
	
	/**
	 * @author ying
	 * @return
	 */
	public String completeBatch() {
		setStatus(0);
		Object object =
			FacesContext
				.getCurrentInstance()
					.getExternalContext()
						.getSessionMap()
							.get("userBean");
		int privilege = 0;
		int pi = 0;
		this.userName = null;
		UserBean ub = null;
		if (object != null) {
			ub = (UserBean)object;
			if(null != ub.getUser()) {
				privilege = ub.getUser().getUserPrivilege();
				pi = ub.getUser().getUserPi();
				this.userName = ub.getUserName();
			}
		}
		int DBStatus = 1;
		String[] batch = new String[]{(String)CookieOperations.getCookieValue("BATCHID")};
		String[] freeIds = batchAnnotationAssembler.getUnlockedBatchList(batch, ub.getUser());
		if(null == freeIds || freeIds.length < batch.length) {
			setStatus(3);
		} else {
			setStatus(0);
		}
		
		if(privilege >= 3 && null != freeIds) {
			DBStatus = batchAnnotationAssembler.completeBatches(freeIds, ub.getUser());
			if(DBStatus == 1) {
				// send email to notify editors
				String fromEmailAddress = "GUDMAP-DB@hgu.mrc.ac.uk";
				String toEmailAddress = Globals.getGudmapEditorsEmail();
//				String toEmailAddress = "xingjun@hgu.mrc.ac.uk";
				String subject = "One Batch has been submitted by " + this.userName;
				String content = "";
				try {
//					System.out.println("sending email!!!!");
					Utility.sendEmail(fromEmailAddress, toEmailAddress, subject, content);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return MessageBean.showMessage("<h3>Selected batch has been successfully submitted to the GUDMAP database and the editorial office. The allocated batch ID is "+StringUtils.join(freeIds, ",")+"</h3>\n" , "DatabaseHome");
			} else {
				return MessageBean.showMessage("<h3>Error occurred, contact gudmap-db@hgu.mrc.ac.uk by referring batch ID: "+StringUtils.join(freeIds, ",")+", click continue to go back to the previous page</h3>\n" , "annotationBatch");
			}
		}
		return null;
	} // end of completeBatch

	public String getBatchID() {
		String temp = (String)FacesUtil.getRequestParamValue("batchID");
		if(null == temp || temp.equals(""))
			batchID = (String)CookieOperations.getCookieValue("BATCHID");
		return batchID;
	}

	public void setBatchID(String batchID) {
		this.batchID = batchID;
	}
	
	public String getLabID() {
		return (String)CookieOperations.getCookieValue("LABID");
	}
	
	public boolean isBatchCompleted() {
		return this.batchCompleted;
	}
	
	public void setBatchCompleted(boolean batchCompleted) {
		this.batchCompleted = batchCompleted;
	}
}
