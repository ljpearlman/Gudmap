package gmerg.beans;

import gmerg.assemblers.BatchAnnotationAssembler;
import gmerg.entities.Globals;
import gmerg.entities.submission.ish.ISHBatch;
import gmerg.utils.CookieOperations;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;

import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;

import javax.faces.context.FacesContext;

public class ISHBatchListBean {
    private boolean debug = false;

	private ISHBatch[] batches;
	private BatchAnnotationAssembler batchAnnotationAssembler;
	private int status;
	//0 normal; 1 no id; 2 only one id is allowed; 3 can't delete locked id
	private String labName;
	private String labID;
	private String userName;
	
	
	public ISHBatchListBean() {
	    if (debug)
		System.out.println("ISHBatchListBean.constructor");

        Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("UserBean");
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
        batchAnnotationAssembler = new BatchAnnotationAssembler();
        String labID = (String)FacesUtil.getRequestParamValue("labId");
        String labName = (String)FacesUtil.getRequestParamValue("labName");
        if(null != labID && !labID.equals("")) {
        	pi = Integer.parseInt(labID);
        	CookieOperations.replaceCookieValues("LABID", new String[]{String.valueOf(pi)});
		} else if(privilege >= 5) {
			try {
				pi = Integer.parseInt((String)CookieOperations.getCookieValue("LABID"));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}  
        if(null != labName && !labName.equals("")) {
        	CookieOperations.replaceCookieValues("LABNAME", new String[]{labName});
        	setLabName(labName);
		} 
        
        //System.out.println("LENGTH TWO :"+privilege+":"+labID+":"+ub.getUser());
        if(privilege >= 3) {
        	
        	batches = batchAnnotationAssembler.getBatchListByPI(String.valueOf(pi), ub.getUser());// submissions with batch ID
        	
        }
        //System.out.println("LENGTH THREE :"+batches);
	}

	public ISHBatch[] getBatches() {
	//System.out.println("ISHBatchSubmission[] getSubmissions is called");
		return batches;
	}

	public void setBatches(ISHBatch[] batches) {
		this.batches = batches;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public String deleteBatch() {
		//System.out.println("LENGTH FOUR :"+(String)FacesUtil.getRequestParamValue("labId"));
        Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("UserBean");
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
        //System.out.println("LENGTH:"+batches);
        String[] selectedIds = checkSelected(batches);
        String[] freeIds = null;
        if(null != selectedIds) {
        	freeIds = batchAnnotationAssembler.getUnlockedBatchList(selectedIds, ub.getUser());
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
//        	System.out.println("deleteBatch: selectedIds is null");
        }
		if(privilege >= 5) {
			try {
				pi = Integer.parseInt((String)CookieOperations.getCookieValue("LABID"));
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
//		System.out.println("PI:"+pi);
		if(privilege >= 3 && null != freeIds) {			
            batchAnnotationAssembler.deleteBatch(freeIds, ub.getUser());
            batches = batchAnnotationAssembler.getBatchListByPI(String.valueOf(pi), ub.getUser());
        }		
		return "return";
	}
	
	public String[] checkSelected(ISHBatch[] batches) {
		ArrayList<ISHBatch> list = new ArrayList<ISHBatch>();
		if(null != batches) {
			for(int i = 0; i < batches.length; i++) {
				if(batches[i].isSelected()) {
					list.add(batches[i]);
				}
			}
		}
		if(list.size() == 0) {
			return null;
		}
		String[] selectedIds = new String[list.size()];
		for(int i = 0; i < list.size(); i++) {
			selectedIds[i] = ((ISHBatch)list.get(i)).getBatchID();
		}
		return selectedIds;
	}
	
	public String addBatch() {
		setStatus(0);
	
        Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("UserBean");
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
        if(privilege >= 5) {
        	try {
				pi = Integer.parseInt((String)CookieOperations.getCookieValue("LABID"));
	        } catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
        if(privilege >= 3) {
//        	System.out.println("addBatch:"+pi);
        	//if(null != batch) {
            batchAnnotationAssembler.addBatch(String.valueOf(pi), ub.getUser());
            batches = batchAnnotationAssembler.getBatchListByPI(String.valueOf(pi), ub.getUser());
        	//}
        }		
		return "return";
	}
	
	/**
	 * @author ying
	 * <p>modified by xingjun - 03/03/2009 
	 * - added code to send email to notify the editors</p>
	 * <p>modified by xingjun - 06/08/2009
	 * - modified code to allow editors to complete batch from the batch list page</p>
	 * @return
	 */
	public String completeBatch() {
//		System.out.println("clicked on the complteBatch button######");
		setStatus(0);
        Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("UserBean");
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
        
		String[] selectedIds = checkSelected(batches);
		String[] freeIds = null;
	        
//        if(privilege >= 3 && pi > 0) { 
		// modified by xingjun - 06/08/2009
		// should allow editors to complete batch from the batch list page
        if(privilege >= 3) {
        	if(null != selectedIds) {
            	freeIds = batchAnnotationAssembler.getUnlockedBatchList(selectedIds, ub.getUser());
            	if(null == freeIds || freeIds.length < selectedIds.length) {
            		setStatus(3);
            	} else {
            		setStatus(0);        		
            	}
//            	if(null != freeIds) {
//            		for(int i = 0; i < freeIds.length; i++) {
//            			System.out.println("freeIds:" + i + ":" + freeIds[i]);
//	           		}
//	           	}
//	           	for(int i = 0; i < selectedIds.length; i++) {
//	           		System.out.println("selectedIds:" + i + ":" + selectedIds[i]);
//	           	}
            	
            } else {
            	setStatus(1);
//            	System.out.println("completeBatch:selectedIds is null");
            	return null;
            }
        	if(null != freeIds) {
        		DBStatus = batchAnnotationAssembler.completeBatches(freeIds, ub.getUser());
        		if(DBStatus == 1) {
        			// send email to notify editors
		    	    String fromEmailAddress = "GUDMAP-DB@hgu.mrc.ac.uk"; 
		    	    String toEmailAddress = Globals.getGudmapEditorsEmail();
//		    	    String toEmailAddress = "xingjun@hgu.mrc.ac.uk";
		    	    String subject = "One Batch has been submitted by " + this.userName;
		    	    String content = "";
		    	    try {
//		    	    	System.out.println("sending email!!!!");
		    	    	Utility.sendEmail(fromEmailAddress, toEmailAddress, subject, content);
		    	    } catch (Exception e) {
		    	    	e.printStackTrace();
		    	    }
		        	return MessageBean.showMessage("<h3>Selected batch has been successfully submitted to the GUDMAP database and the editorial office. The allocated batch ID is "+StringUtils.join(freeIds, ",")+"</h3>\n" , "DatabaseHome");
		        } else {
		        	return MessageBean.showMessage("<h3>Error occurred, contact gudmap-db@hgu.mrc.ac.uk by referring batch ID: "+StringUtils.join(freeIds, ",")+", click continue to go back to the previous page</h3>\n" , "annotationBatchList");
		        }
	        }
        }
		return "return";
	} // end of completeBatch
	
	public String getLabID() {
		String temp = (String)FacesUtil.getRequestParamValue("labId");
		if(null == temp || temp.equals(""))
			labID = (String)CookieOperations.getCookieValue("LABID");
		return labID;
	}
	
	public void setLabID(String labId) {
		this.labID = labId;
	}
	
	public String getLabName() {
		String temp = (String)FacesUtil.getRequestParamValue("labName");
		
		if(null == temp || temp.equals(""))
			labName = (String)CookieOperations.getCookieValue("LABNAME");
		return labName;
	}

	public void setLabName(String labName) {
		this.labName = labName;
	}
}
