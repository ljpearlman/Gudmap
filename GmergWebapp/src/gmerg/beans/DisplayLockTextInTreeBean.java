package gmerg.beans;

import gmerg.entities.submission.LockingInfo;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;
import gmerg.utils.CookieOperations;
import gmerg.assemblers.EditAssemblerUtil;
import gmerg.assemblers.EditExpressionAssembler;
import gmerg.entities.User;

public class DisplayLockTextInTreeBean {
	private String lockingText;
    private String lockingTextDisplayStyle;
    private LockingInfo lockingInfo;
    private boolean lockingSwitchOn;
    private String userName;
    private User user;
    private EditExpressionAssembler editExpressionAssembler;
    
    public DisplayLockTextInTreeBean() {
    	editExpressionAssembler = new EditExpressionAssembler();
    	Object object = FacesUtil.getSessionValue("userBean");
        if (object != null) {
        	UserBean ub = (UserBean)object;
        	this.user = ub.getUser();// added by xingjun - 15/08/2008 - need pass userId to locking related method
        	this.userName = ub.getUserName();
        } else {
        	this.userName = null;
        }
        
    	String submissionId = FacesUtil.getRequestParamValue("id");
    	if(null != submissionId && !submissionId.equals("")) {
			CookieOperations.replaceCookieValues("TEMPSUBID", new String[]{submissionId});
		} else {
			submissionId = (String)CookieOperations.getCookieValue("TEMPSUBID");
		}
    	String lockingSwitchOn = Utility.getLockingSwitchOn();
    	if (lockingSwitchOn.equals("true")) { // locking function switched on
    		this.lockingSwitchOn = true;
        	// lock the submission if it's unlocked
        	this.lockingInfo = editExpressionAssembler.getLockingInfo(submissionId);
//    		System.out.println("lockinfo obtained in edit expressionbean.");
        	if (this.lockingInfo != null) {
        		String lb = this.lockingInfo.getLockedBy();
        		int lockTime = this.lockingInfo.getLockTime();
        		int definedLockTimOut = Integer.parseInt(Utility.getLockTimeOut());
//        		System.out.println("eeBean:time elapse:" + (lockTime-this.lockTimeOut));
//        		System.out.println("userName: " + userName);
        		if (!this.userName.equals(lb) 
        				&& (lockTime-definedLockTimOut < 0 || lockTime-definedLockTimOut == 0)) {
//        			this.submissionLocked =  true;
        			this.lockingText = "LOCKED by " + lb + ". Cannot modify";
        			this.lockingTextDisplayStyle = Utility.getLockedTextDisplayStyle();
        		} else {
        			// update lock time
//        			int submissionLocked =
//        				EditAssemblerUtil.relockSubmission(submissionId, this.userName);
        			int submissionLocked =
        				EditAssemblerUtil.relockSubmission(submissionId, this.user);
        			if (submissionLocked == 0) { // failed to re-lock
        				//this.errorCode = 7;
        			} else {
        				//this.errorCode = 0;
//                		this.submissionLocked = false;
            			this.lockingText = "LOCKED by " + lb;
            			this.lockingTextDisplayStyle = Utility.getLockedTextDisplayStyle();
        			}
        		}
        	} else { // unlocked - need to lock
        		//modified by Ying 20080908
/*        		System.out.println("lock submission by eebean: initialise stage!");
//    			int submissionLocked =
//    				EditAssemblerUtil.lockSubmission(submissionId, this.userName);
    			int submissionLocked =
    				EditAssemblerUtil.lockSubmission(submissionId, this.user);
    			if (submissionLocked == 0) { // failed to lock
    				//this.errorCode = 7;
    			} else {
        			//this.errorCode = 0;
//        			this.submissionLocked = false;
        			this.lockingText = "LOCKED by " + this.userName;
        			this.lockingTextDisplayStyle = Utility.getLockedTextDisplayStyle();
    			}*/
        	}
    	} else {
    		this.lockingSwitchOn = false;
    	}
    }
    
    
    public String getLockingText() {
    	return this.lockingText;
    }
    
    public void setLockingText(String lockingText) {
    	this.lockingText = lockingText;
    }
    
    public LockingInfo getLockingInfo() {
    	return this.lockingInfo;
    }
    
    public void setLockingInfo(LockingInfo lockingInfo) {
    	this.lockingInfo = lockingInfo;
    }
    
    public String getLockedBy() {
    	return this.lockingInfo.getLockedBy();
    }
    
    public String getLockingTextDisplayStyle() {
    	return this.lockingTextDisplayStyle;
    }
    
    public void setLockingTextDisplayStyle(String lockingTextDisplayStyle) {
    	this.lockingTextDisplayStyle = lockingTextDisplayStyle;
    }
    
    public boolean isLockingSwitchOn() {
    	return this.lockingSwitchOn;
    }    
}
