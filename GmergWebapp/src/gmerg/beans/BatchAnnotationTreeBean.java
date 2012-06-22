package gmerg.beans;

import gmerg.assemblers.AnnotationTestAssembler;
import gmerg.assemblers.EditAssemblerUtil;
import gmerg.entities.submission.LockingInfo;
import gmerg.entities.submission.Submission;
import gmerg.entities.User;
import gmerg.utils.CookieOperations;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

public class BatchAnnotationTreeBean {
    private boolean debug = false;
	private SelectItem [] stagesInPerspective;
	private AnnotationTestAssembler annotationTestAssembler;
	private String stage;
	private ArrayList<String> treeContent;
	private String subId;
	private Submission submission;
	private int status;
	//0 normal; 2 conflict; 
	
	// added by xingjun - 05/08/2008
	// dont display locking info at moment. might need to do in the future
    // locking properties
    private String lockingText;
    private String lockingTextDisplayStyle;
    private LockingInfo lockingInfo;
    private boolean lockingSwitchOn;
    
    // xingjun - 15/08/2008
    // changed code related to locking function - they new use user instead of userName
    private User user;
    

    // xingjun - 20/07/2011 - need to check nullable situation for submission
	public BatchAnnotationTreeBean() {
	    if (debug)
		System.out.println("BatchAnnotationTreeBean.constructor");

		annotationTestAssembler = new AnnotationTestAssembler();
		ResourceBundle bundle = ResourceBundle.getBundle("configuration");
		String subIdParam = getSubIdCookie();
		String stageInDB = null;
		if(null != subIdParam && !subIdParam.trim().equals("")) {
//			stageInDB = annotationTestAssembler.getStageFromSubmission(subIdParam);
			this.submission = annotationTestAssembler.getSubmissionSummary(subIdParam);
			if (submission != null) {
				stageInDB = this.submission.getStage();
				CookieOperations.replaceCookieValues("TEMPSUBID", new String[]{subIdParam});
			}
		}
		if(null != stageInDB ) {
			if(stageInDB.trim().equals("")) {
				CookieOperations.replaceCookieValues("STAGEINDB", new String[]{""});
			} else {
				CookieOperations.replaceCookieValues("STAGEINDB", new String[]{bundle.getString("stage_series_short") + stageInDB});		
			}
		}
//		System.out.println("BatchAnnotationBean:subIdParam: " + subIdParam);
		if(null != stageInDB && null != subIdParam && !stageInDB.trim().equals("") && !subIdParam.trim().equals("")) {
			// xingjun - 08/03/2010 - no need to add prefix 'TS' before the stage value
//			setStage(bundle.getString("stage_series_short") + stageInDB);
			setStage(stageInDB);
			setSubId(subIdParam);
//			treeContent = annotationTestAssembler.getTreeContent(bundle.getString("stage_series_short") + stageInDB, subIdParam);
			treeContent = annotationTestAssembler.getTreeContent(stageInDB, subIdParam);
		}

		// added by xingjun - 05/08/2008
		Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("userBean");
        UserBean ub = null;
        if (object != null) {
        	ub = (UserBean)object;
        	if(null != ub.getUser()) {
        		this.user = ub.getUser();
        	}
        }
	}
	
	public ArrayList<String> getAnatomyTreeContent() {
		return treeContent;
	}
	
	/**
	 * <p>xingjun - 14/05/2010 - added dpc stage value into the labels</p>
	 * @return
	 */
	public SelectItem [] getStagesInPerspective() {
		ArrayList stages = annotationTestAssembler.getStagesInPerspective();
		stagesInPerspective = new SelectItem [stages.size()];
//		for(int i=0;i<stagesInPerspective.length;i++){
//            String [] stage = (String []) stages.get(i);
//            stagesInPerspective[i] = new SelectItem(stage[0]);
//        }
		for(int i=0;i<stagesInPerspective.length;i++){
            String [] stage = (String []) stages.get(i);
            stagesInPerspective[i] = new SelectItem(stage[0], (stage[0] + "\t---\t" + stage[1]));
        }
		return stagesInPerspective;
	}
	
	// xingjun - 20/07/2011 - need to check the nullable situation
	public void setStage(String stage){
		if (this.submission != null) {
			this.submission.setStage(stage);
		}
		this.stage = stage;
	}
	
	public String getStage() {
//		return this.submission.getStage();
		return this.stage;
	}
	

	
	public String displayTree() {
		Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("userBean");
        int privilege = 0;
        int pi = 0;
        UserBean ub = null;
        if (object != null) {
        	ub = (UserBean)object;
        	if(null != ub.getUser()) {
        		privilege = ub.getUser().getUserPrivilege();
        		pi = ub.getUser().getUserPi();
        	}
        }

        String subIdParam = (String)CookieOperations.getCookieValue("TEMPSUBID");
//        System.out.println("BatchAnnotationTreeBean:displayTree:stage: " + stage);
		int stageStatus = annotationTestAssembler.changeStage(stage, subIdParam, ub.getUser());
		
		if(stageStatus == 1 && null != stage && !stage.equals("")) {
			CookieOperations.replaceCookieValues("STAGEINDB", new String[]{stage});
		}
        treeContent = annotationTestAssembler.getTreeContent(stage, subIdParam);
        return null;
    }
	
	public void addExpressionAnnotation(ActionEvent e) {
		setStatus(0);
//		Object object =
//            FacesContext
//                .getCurrentInstance()
//                    .getExternalContext()
//                        .getSessionMap()
//                            .get("userBean");
//        int privilege = 0;
//        int pi = 0;
//        UserBean ub = null;
//        if (object != null) {
//        	ub = (UserBean)object;
//        	if(null != ub.getUser()) {
//        		privilege = ub.getUser().getUserPrivilege();
//        		pi = ub.getUser().getUserPi();
//        	}
//        }
		// modified by xingjun - 05/08/2003 - get the user bean when the bean is initialised
		int privilege = (this.user == null) ? 0:this.user.getUserPrivilege();
		int pi = (this.user == null) ? 0:this.user.getUserPi();
        
		String subIdParam = getSubIdCookie();
		String stageParam = FacesUtil.getRequestParamValue("stage");
		FacesContext context = FacesContext.getCurrentInstance();
		String clientId = e.getComponent().getClientId(context);
		int index = clientId.indexOf(":");
		String expression = clientId.substring(index+1);
		
		String [] compIds = FacesUtil.getRequestParamValues("component");
//		System.out.println("COMPONENT :"+compIds.length+":"+subIdParam+":"+stageParam+":"+expression);
		if(compIds != null && compIds.length >= 1 && privilege >= 3) {
//			for(int i = 0; i < compIds.length; i++) {
//				System.out.println("COMPONENT :"+i+":"+compIds[i]);
//			}
			/*update the values to the database
			 * only if the user has clicked components.
			 * If they haven't then they will just see the page with the tree again.
			 * the method will be something like 
			 * annotationTestAssembler.updateStrengthAnnotationForSubmission(compIds, submissionId, expression);
			 * note expression will be a value of p, s, m, w, u and nd for present, strong, moderate, weak, uncertain and not detected
			 * compIds will be and array of emap ids
			 * submissionid will be the gudmap id
			 * user can be obtained from session in the annotationTestAssembler or DAO or you can add user to be passed along with this method.
			*/
			// modified by xingjun - 05/08/2008
			// 1. get the user bean when the bean is initialised
			// 2. if the submission is locked by other people, do nothing; or lock the submission first
//			int expressionUpdated = annotationTestAssembler.updateExpression(compIds, subIdParam, expression, stageParam, ub.getUser());
//	        if (this.lockSubmission(subIdParam)) {
//			System.out.println("HERE:"+subIdParam+compIds.length+expression+this.user.getUserName());
				int expressionUpdated = annotationTestAssembler.updateExpression(compIds, subIdParam, expression, stageParam, this.user);
//				System.out.println("DBSTATUS:" + expressionUpdated);
				if(expressionUpdated==2) {
					setStatus(2);
				}
				// xingjun - 23/07/2008 - start
				// if expression value is set to 'not detected', 'uncertain' or 'not examined',
				// pattern should be null
				else if (expressionUpdated == 1) {
					if (expression.equals("nd") || expression.equals("u") || expression.equals("ne")) {
				        Object object =
				            FacesContext
				                .getCurrentInstance()
				                    .getExternalContext()
				                        .getSessionMap()
				                            .get("editExpressionBean");

				        if (object != null) {
				    		EditExpressionBean eeBean = (EditExpressionBean) object;
				    		eeBean.setPatterns(null);
				    		if (expression.equals("ne")) {
				    			eeBean.setNotes(null);
				    		}
				        }
					}
				}
				// xingjun - 23/07/2008 - end
//	        } // xingjun - 05/08/2008
		}
		displayTree();
	}

	public void addPattern(ActionEvent e) {
		setStatus(0);
		int privilege = (this.user == null) ? 0:this.user.getUserPrivilege();
		int pi = (this.user == null) ? 0:this.user.getUserPi();
        
		String subIdParam = getSubIdCookie();
		String stageParam = FacesUtil.getRequestParamValue("stage");
		FacesContext context = FacesContext.getCurrentInstance();
		String clientId = e.getComponent().getClientId(context);
		int index = clientId.indexOf(":");
		String pattern = clientId.substring(index+1);
		//System.out.println("PATTERN:"+pattern);
		if(pattern.equals("sc"))
			pattern = "single cell";
		String [] compIds = FacesUtil.getRequestParamValues("component");
		if(compIds != null && compIds.length >= 1 && privilege >= 3) {
			int expressionUpdated = annotationTestAssembler.updatePatterns(compIds, subIdParam, pattern, stageParam, this.user);
			//System.out.println("RETURN:"+expressionUpdated);
			if(expressionUpdated==2) {
				setStatus(2);
			}
		}
		displayTree();	
	}
	
	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}
	
	public Submission getSubmission() {
		return this.submission;
	}
	
	public void setSubmission(Submission submission) {
		this.submission = submission;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getSubIdCookie() {
		String subIdParam = FacesUtil.getRequestParamValue("id");
		if(null != subIdParam){ 
			return subIdParam;
		} else {
			return (String)CookieOperations.getCookieValue("TEMPSUBID");
		}
	}
	
	/**
	 * @author xingjun - 05/08/2008
	 * <p>if the submission is locked, do nothing; or lock the submission</p>
	 * 
	 * @param submissionId
	 * @return
	 */
	public boolean lockSubmission(String submissionId) {
		String lockingSwitchOn = Utility.getLockingSwitchOn();
        if (lockingSwitchOn.equals("true")) { // locking function switch on
            // check locking info - do nothing if locked, lock the submission if unlocked
        	this.lockingInfo = EditAssemblerUtil.getLockingInfo(submissionId);
        	if (this.lockingInfo != null) {
        		String lb = this.lockingInfo.getLockedBy();
        		int lockTime = this.lockingInfo.getLockTime();
        		int definedLockTimeOut = Integer.parseInt(Utility.getLockTimeOut());
        		// might be locked by others
        		if (!this.user.getUserName().equals(lb) 
        				&& (lockTime-definedLockTimeOut < 0 || lockTime-definedLockTimeOut == 0)) { // locked
//        			this.submissionLocked =  true;
        			this.lockingText = "LOCKED by " + lb + ". Cannot modify";
        			this.lockingTextDisplayStyle = Utility.getLockedTextDisplayStyle();
        			return false;
        		} else { // re-lock - update lock time;
//        			int submissionLocked =
//        				EditAssemblerUtil.relockSubmission(submissionId, this.user.getUserName());
        			int submissionLocked =
        				EditAssemblerUtil.relockSubmission(submissionId, this.user);
        			if (submissionLocked == 0) { // failed to re-lock
//        				this.errorCode = 7;
        				return false;
        			}
        		}
        	} else { // unlocked - need to lock
//        		System.out.println("lock submission by eebean: update expression!");
//    			int submissionLocked =
//    				EditAssemblerUtil.lockSubmission(submissionId, this.user.getUserName());
    			int submissionLocked =
    				EditAssemblerUtil.lockSubmission(submissionId, this.user);
    			if (submissionLocked == 0) { // failed to lock
//    				this.errorCode = 7;
    				return false;
    			}
        	}
//    		this.errorCode = 0;
//    		this.submissionLocked =  false;
    		this.lockingText = "LOCKED by " + this.user.getUserName();
    		this.lockingTextDisplayStyle = Utility.getLockedTextDisplayStyle();
        }
        // successful
        return true;
	}

}
