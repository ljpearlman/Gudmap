package gmerg.beans;

import gmerg.assemblers.EditSubmissionAssembler;
import gmerg.assemblers.LabSummaryAssembler;
import gmerg.entities.summary.LabSummary;
import gmerg.entities.User;
import gmerg.utils.FacesUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.faces.context.FacesContext;

public class LabSummaryBean {
    private boolean debug = false;

    LabSummaryAssembler assembler = null;
    private LabSummary[] labs;
    
    // xingjun - 25/11/2009 - at moment hard coded here, may need to be moved to db in future
    private String[] labToBeViewedByExaminer = {"8", "35", "50"};
    private User user;
    
    /*
     * Get all the lab information using LabSummaryAssembler
     */

    public LabSummaryBean() {
	if (debug)
	    System.out.println("LabSummaryBean.constructor");

    }

    /**
     * <p>xingjun - 22/04/2010 - change from 'Editing in Progress' to 'Not Public'</p>
     * <p>xingjun - 31/05/2011 - added archive ids into display</p>
     * @param old
     * @return
     */
    public ArrayList convertArray(ArrayList old) {
    	if(null == old)
    		return null;
    	ArrayList<Object[]> newList = new ArrayList<Object[]>();
    	DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
    	SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    	String date = null;
    	Object[] newRow = null;
    	
    	Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("userBean");
        int privilege = 0;
        if (object != null) {
        	UserBean ub = (UserBean)object;
        	this.user = ub.getUser();
        	if (user != null) {
        		privilege = user.getUserPrivilege();
        	}
        }
        
        for(int i = 0; i < old.size(); i++) {  
        	try {
        	date = df.format(df2.parse((String)((Object[])old.get(i))[0]));
            } catch (Exception e) {
        		e.printStackTrace();
        	}
            
            if(privilege >= 3 && FacesContext.getCurrentInstance().getViewRoot().getViewId().equalsIgnoreCase("/pages/edit_entry_page_per_lab.jsp")) {
            	    if(((Object[])old.get(i))[3].equals("4")){
                		newRow = new Object[]{(Object)date, 
                    			((Object[])old.get(i))[1],
                    			((Object[])old.get(i))[2], 
                    			"Public",
                    			((Object[])old.get(i))[4]};// xingjun - 31/05/2011 - added archive ids into display
                	} else if(((Object[])old.get(i))[3].equals("5")){
                		newRow = new Object[]{(Object)date, 
                    			((Object[])old.get(i))[1],
                    			((Object[])old.get(i))[2], 
                    			"Awaiting Annotation",
                    			((Object[])old.get(i))[4]};// xingjun - 31/05/2011 - added archive ids into display
                	} else if(((Object[])old.get(i))[3].equals("19")){
                		newRow = new Object[]{(Object)date, 
                    			((Object[])old.get(i))[1],
                    			((Object[])old.get(i))[2], 
                    			"Partially Annotated by Annotator",
                    			((Object[])old.get(i))[4]};// xingjun - 31/05/2011 - added archive ids into display
                	} else if(((Object[])old.get(i))[3].equals("20")){
                		newRow = new Object[]{(Object)date, 
                    			((Object[])old.get(i))[1],
                    			((Object[])old.get(i))[2], 
                    			"Completely Annotated by Annotator",
                    			((Object[])old.get(i))[4]};// xingjun - 31/05/2011 - added archive ids into display
                	} else if(((Object[])old.get(i))[3].equals("21")){
                		newRow = new Object[]{(Object)date, 
                    			((Object[])old.get(i))[1],
                    			((Object[])old.get(i))[2], 
                    			"Partially Annotated by Sr. Annotator",
                    			((Object[])old.get(i))[4]};// xingjun - 31/05/2011 - added archive ids into display
                	} else if(((Object[])old.get(i))[3].equals("22")){
                		newRow = new Object[]{(Object)date, 
                    			((Object[])old.get(i))[1],
                    			((Object[])old.get(i))[2], 
                    			"Awaiting Editor QA",
                    			((Object[])old.get(i))[4]};// xingjun - 31/05/2011 - added archive ids into display
                	} else if(((Object[])old.get(i))[3].equals("23")){
                		newRow = new Object[]{(Object)date, 
                    			((Object[])old.get(i))[1],
                    			((Object[])old.get(i))[2], 
                    			"Partially Annotated by Editor",
                    			((Object[])old.get(i))[4]};// xingjun - 31/05/2011 - added archive ids into display
                	} else if(((Object[])old.get(i))[3].equals("24")){
                		newRow = new Object[]{(Object)date, 
                    			((Object[])old.get(i))[1],
                    			((Object[])old.get(i))[2], 
                    			"Completely Annotated by Editor",
                    			((Object[])old.get(i))[4]};// xingjun - 31/05/2011 - added archive ids into display
                	} else if(((Object[])old.get(i))[3].equals("25")){
                		newRow = new Object[]{(Object)date, 
                    			((Object[])old.get(i))[1],
                    			((Object[])old.get(i))[2], 
                    			"Partially Annotated by Sr. Editor",
                    			((Object[])old.get(i))[4]};// xingjun - 31/05/2011 - added archive ids into display
                	}
            } else {
            	if(((Object[])old.get(i))[3].equals("0")) {
        	    	newRow = new Object[]{(Object)date, 
        	    			((Object[])old.get(i))[1],
        	    			((Object[])old.get(i))[2], 
//            				"Editing in Progress"};
        	    			"Not Public",
        	    			((Object[])old.get(i))[4]};
            	} else if(((Object[])old.get(i))[3].equals("1")){
            		newRow = new Object[]{(Object)date, 
                			((Object[])old.get(i))[1],
                			((Object[])old.get(i))[2], 
                			"Available",
                			((Object[])old.get(i))[4]}; // added by Bernie - 24/3/2011 - mantis 445       		
            	}
            }

            if (newRow != null) {
                newList.add(newRow);
            }
        	newRow = null;
        }
    	return newList;
    }    
    
	/*
	 * Return a list of LabSummary object to the lab_summary.jsp page
	 */
	public LabSummary[] getLabs() {
		ArrayList results = new ArrayList();
    	assembler = new LabSummaryAssembler();
		labs = assembler.getSummaryData();
//		System.out.println("lab number: " + labs.length);
		for(int i = 0; i < labs.length; i++) {
			if(null != labs[i]) {
    			results = labs[i].getSummaryResults();
     			results = convertArray(results);
     			labs[i].setSummaryResults(results);
			}				
		}
		return labs;
	}
	
	public LabSummary[] getSelectedLab() {
		ArrayList results = new ArrayList();
		Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("userBean");
        String PI = null;
        int privilege = 0;
        if (object != null) {
        	UserBean ub = (UserBean)object;
        	if(null != ub.getUser()) {
        		privilege = ub.getUser().getUserPrivilege();
        		PI = String.valueOf(ub.getUser().getUserPi());
        	}
        }
        //modified by ying
        ArrayList<LabSummary> newlabs = new ArrayList<LabSummary>();
    	assembler = new LabSummaryAssembler();
		labs = assembler.getSummaryDataPerLab(PI, privilege);
		for(int i = 0; i < labs.length; i++) {
//			System.out.println("labName: " + labs[i].getLabName());
			if(null != labs[i]) {
    			results = labs[i].getSummaryResults();
    			results = convertArray(results);
    			labs[i].setSummaryResults(results);
    			
    			// xingjun - 25/2009 - start
    			String userType = this.user.getUserType();
    			if (this.isViewableBySpecifiedExaminer(userType, labs[i].getLabId())) {
    				labs[i].setViewableByExaminer(true);
    			} else {
    				labs[i].setViewableByExaminer(false);
    			}
    			// xingjun - 25/11/2009 - end
    			
    			newlabs.add(labs[i]);
			}				
		}
		LabSummary[] newLabsArray = new LabSummary[newlabs.size()];
		for(int i = 0; i < newlabs.size(); i++) {
			newLabsArray[i] = newlabs.get(i);
		}
		
		return newLabsArray;
	}
	
	/**
	 * @author xingjun - 24/12/2008
	 * <p>make a batch of submission public by lab id and submission date</p>
	 * <p>modified to add extra param: submission state</p>
	 * <p>xingjun - 01/07/2011 - added extra param: archive id</p>
	 * @return
	 */
	public String makeSubmissionsPublic() {
//		System.out.println("LabSummaryBean:makeSubmissionsPublic!!!");
		// obtain the params
    	String labId = FacesUtil.getRequestParamValue("labId");
        String submissionDate = FacesUtil.getRequestParamValue("submissionDate");
        String submissionState = FacesUtil.getRequestParamValue("submissionState");
        String submissionType = FacesUtil.getRequestParamValue("submissionType");
    	String archiveId = FacesUtil.getRequestParamValue("archiveId");
        
        // failed to get needed parameters
        if (labId == null || labId.equals("") 
        		|| submissionDate == null || submissionDate.equals("")
        		|| submissionState == null || submissionState.equals("")
        		|| submissionType == null || submissionType.equals("")
        		|| archiveId == null || archiveId.equals("")) {
//    		System.out.println("LabSummaryBean:makeSubmissionsPublic:cant update!!!");
        	return "cannot update submission status";
        }
        Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("userBean");
        int privilege = 0;
        String userName = null;
        if (object != null) {
        	UserBean ub = (UserBean)object;
        	if(null != ub.getUser()) {
        		privilege = ub.getUser().getUserPrivilege();
        		userName = ub.getUserName();
        	}
        }
        
        // only senior editor can make submissions public
        if(privilege >= 6) {
            EditSubmissionAssembler editSubmissionAssembler = new EditSubmissionAssembler();
            boolean set2Public = true;
//            boolean submissionStatusUpdated = 
//            	editSubmissionAssembler.updateSubmissionStatusByLabAndDateAndState(labId, submissionDate, 4, userName, submissionState, set2Public);
            boolean submissionStatusUpdated = 
            	editSubmissionAssembler.updateSubmissionStatusByLabAndDateAndState(labId, submissionDate, archiveId, 4, userName, submissionState, set2Public);
            if (!submissionStatusUpdated) {
            	return "failed to make submissions public";
            }
        }
		return "given submissions to public";
	}
	
	/**
	 * @author xingjun - 25/11/2009
	 * @param userType
	 * @param labId
	 * @return
	 */
	private boolean isViewableBySpecifiedExaminer(String userType, String labId) {
//		System.out.println("labSummaryBean:isViewableBySpecifiedExaminer:userType: " + userType);
//		System.out.println("labSummaryBean:isViewableBySpecifiedExaminer:labId: " + userType);
		boolean result = false;
		if (userType.equals("EXAMINER")) {
			int len = this.labToBeViewedByExaminer.length;
			for (int i=0;i<len;i++) {
				if (labId.equals(this.labToBeViewedByExaminer[i])) {
					result = true;
				}
			}
		}
//		System.out.println("labSummaryBean:isViewableBySpecifiedExaminer:result: " + result);
		return result;
	}
	
}
