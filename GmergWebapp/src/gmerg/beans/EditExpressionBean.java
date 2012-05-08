/**
 * 
 */
package gmerg.beans;

import gmerg.assemblers.EditExpressionAssembler;
import gmerg.assemblers.EditAssemblerUtil;
import gmerg.entities.submission.ExpressionDetail;
import gmerg.entities.submission.ExpressionPattern;
import gmerg.entities.submission.LockingInfo;
import gmerg.entities.User;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;

import java.util.ArrayList;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
//import javax.faces.event.ActionEvent;

/**
 * @author xingjun
 *
 */
public class EditExpressionBean {

    private SelectItem[] strengthItems =  {
    		new SelectItem("", "not examined"),
    	    new SelectItem("present", "present"),
    	    new SelectItem("not detected", "not detected"),
    	    new SelectItem("uncertain", "uncertain")
    };
    
    private SelectItem[] additionalStrengthItems = {
    		new SelectItem("", ""),
    		new SelectItem("moderate", "moderate"),
    		new SelectItem("strong", "strong"),
    		new SelectItem("weak", "weak")
    };
    
    private SelectItem[] patternItems;
    private SelectItem[] locationItems;
    private SelectItem[] componentItems;
    
	private ExpressionDetail expression;
    private EditExpressionAssembler editExpressionAssembler;
    private ExpressionPattern[] patterns;
    private boolean addingPattern = false;
    private boolean noPatternCheckedForDeletion;
//    private boolean anyPatternSelected;
    private boolean patternAdded = false;
    private String submissionId;
    private String componentId;
    private boolean expressionUpdated = false;
    private String userName;
    // added by xingjun - 15/08/2008
    // also changed code related to locking function - they new use user instead of userName
    private User user;
    ////////////////////////////////
    
    // ///////////////////////////////////
    // errorCode = 0 no error
    // errorCode = 1 - expression confliction with ancsestor - present
    // errorCode = 2 - expression conflict with decendent - not detected
    // errorCode = 3 - expression confliction with ancsestor - uncertain
    // errorCode = 4 - user try to modify a locked submission
    // errorCode = 5 - user try to unlock a submission which s/he has no privilege to do
    // errorCode = 7 - failed to update database
    /////////////////////////////////////
    private int errorCode = 0;
    private boolean updateButtonClicked;
    
    // locking properties
    private boolean submissionLocked;
    private boolean unlocking;
    private String lockingText;
    private String lockingTextDisplayStyle;
    private LockingInfo lockingInfo;
    private boolean lockingSwitchOn;
    
    
    /**
     * constructor
     * <p>modified by xingjun @ 13/06/2008 - added code to obtain pattern and location list on the fly</p>
     * <p>modified by xingjun - 28/09/2009
     * - need to check if expression is null before go to find pattern info</p>
     * 
     */
    public EditExpressionBean() {
//    	System.out.println("========EditExpressionBean initialised--------");

    	// get user name
//    	this.userName = (String)FacesUtil.getSessionValue("loggedInUserName");
    	FacesUtil.setSessionValue("addingPattern", "false");
        Object object = FacesUtil.getSessionValue("userBean");
        if (object != null) {
        	UserBean ub = (UserBean)object;
        	this.user = ub.getUser();// added by xingjun - 15/08/2008 - need pass userId to locking related method
        	this.userName = ub.getUserName();
        } else {
        	this.userName = null;
        }

        // get pattern item list
        editExpressionAssembler = new EditExpressionAssembler();
        ArrayList patternList = editExpressionAssembler.getPatternList();
        int len = patternList.size() + 1;
        patternItems = new SelectItem[len];
        patternItems[0] = new SelectItem("", "");
        for (int i=1;i<len;i++) {
        	patternItems[i] =
        		new SelectItem(patternList.get(i-1).toString(), patternList.get(i-1).toString());
        }
        	
       	// get location item list
        ArrayList locationList = editExpressionAssembler.getLocationList();
        len = locationList.size() + 1;
        locationItems = new SelectItem[len];
        locationItems[0] = new SelectItem("", "");
        for (int i=1;i<len;i++) {
        	locationItems[i] =
        		new SelectItem(locationList.get(i-1).toString(), locationList.get(i-1).toString());
        }
        
        // get submission and component id
        submissionId = FacesUtil.getRequestParamValue("submissionId");
        componentId = FacesUtil.getRequestParamValue("componentId");
//    	System.out.println("subId: " + submissionId);
//    	System.out.println("compId: " + componentId);
        
        if (submissionId != null && !submissionId.equals("")
        		&& componentId != null && !componentId.equals("") ) {
            this.expression = 
            	editExpressionAssembler.getExpression(submissionId, componentId);
            
            // get pattern infomation - modified by xingjun - 28/09/2009 - need to check if expression is null
            if (expression != null && expression.isAnnotated()) {
            	patterns = 
            		editExpressionAssembler.getPatterns(String.valueOf(expression.getExpressionId()));
            }
        }
        
        // get component item list - xingjun - 19/06/2008
        // used to display components for the location drop down list 
        // on the pattern/location editing page
    	// xingjun - 22/08/2008 - start /////////////////
    	// if the bean is initialised from the lab_ish_edit.jsp page
    	// only the submission id will be passed, through 'id' parameter
        // the component id is not, so the expression could not been initialised 
        if (this.expression != null) {
            ArrayList componentList =
            	editExpressionAssembler.getComponentListAtTheStage(this.expression.getStage());
            if (componentList != null && componentList.size() != 0) { // xingjun - 24/11/2009 - the component list may be null
            	len = componentList.size();
            	componentItems = new SelectItem[len];
            	for (int i=0;i<len;i++) {
            		componentItems[i] = new SelectItem(((String[])componentList.get(i))[0].toString(), // value (e.g. 1234) 
            				((String[])componentList.get(i))[1].toString());// label (e.g. mesenchyme(EMAP:7345))
            	}
            } else {
            	componentItems = null; 
            	// xingjun - 08/12/2009 - at least return an empty value - leave it for time being 
//            	componentItems = new SelectItem[1];
//        		componentItems[0] = new SelectItem("","");// give it an empty value
            }
        }
    	// xingjun - 22/08/2008 - end /////////////////
    	
    	String lockingSwitchOn = Utility.getLockingSwitchOn();
    	// xingjun - 22/08/2008 - start /////////////////
    	// if the bean is initialised from the lab_ish_edit.jsp page
    	// the submission id is passed through 'id' parameter
    	if (submissionId == null || submissionId.equals("")) {
            submissionId = FacesUtil.getRequestParamValue("id");
//            System.out.println("EditExpressionBean:submissionId: " + submissionId);
    	}
    	// xingjun - 22/08/2008 -end /////////////////
    	//modified by Ying 20080908
    	/*if (lockingSwitchOn.equals("true")) { // locking function switched on
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
        				this.errorCode = 7;
        			} else {
        				this.errorCode = 0;
//                		this.submissionLocked = false;
            			this.lockingText = "LOCKED by " + lb;
            			this.lockingTextDisplayStyle = Utility.getLockedTextDisplayStyle();
        			}
        		}
        	} else { // unlocked - need to lock
        		System.out.println("lock submission by eebean: initialise stage!");
//    			int submissionLocked =
//    				EditAssemblerUtil.lockSubmission(submissionId, this.userName);
    			int submissionLocked =
    				EditAssemblerUtil.lockSubmission(submissionId, this.user);
    			if (submissionLocked == 0) { // failed to lock
    				this.errorCode = 7;
    			} else {
        			this.errorCode = 0;
//        			this.submissionLocked = false;
        			this.lockingText = "LOCKED by " + this.userName;
        			this.lockingTextDisplayStyle = Utility.getLockedTextDisplayStyle();
    			}
        	}
    	} else {
    		this.lockingSwitchOn = false;
    	}*/
    } // constructor
    
    
    public SelectItem[] getStrengthItems() {
    	return this.strengthItems;
    }
    
    public SelectItem[] getAdditionalStrengthItems() {
    	return this.additionalStrengthItems;
    }
    
    public SelectItem[] getPatternItems() {
    	return this.patternItems;
    }
    
    public SelectItem[] getLocationItems() {
    	return this.locationItems;
    }
    
    public SelectItem[] getComponentItems() {
    	return this.componentItems;
    }
    
    public ExpressionDetail getExpressionDetail() {
    	return this.expression;
    }
    
    public void setExpressionDetail(ExpressionDetail expression) {
    	this.expression = expression;
    }
    
    // xingjun - 21/07/2011 - expression might be null
    public String getPrimaryStrength() {
//    	return this.expression.getPrimaryStrength();
    	String primaryStrength = null;
    	if (this.expression != null) {
    		primaryStrength = this.expression.getPrimaryStrength();
    	}
    	return primaryStrength;
    }
    
    public void setPrimaryStrength(String primaryStrength) {
    	this.expression.setPrimaryStrength(primaryStrength);
    }
    
    // xingjun - 21/07/2011 - expression might be null
    public String getSecondaryStrength() {
//    	return this.expression.getSecondaryStrength();
    	String secondaryStrength = null;
    	if (this.expression != null) {
    		secondaryStrength = this.expression.getSecondaryStrength();
    	}
    	return secondaryStrength;
    }
    
    public void setSecondaryStrength(String secondaryStrength) {
//    	this.secondaryStrength = secondaryStrength;
    	this.expression.setSecondaryStrength(secondaryStrength);
    }
    
    public boolean isPatternAdded() {
    	return this.patternAdded;
    }
    
    public void setPatternAdded(boolean patternAdded) {
    	this.patternAdded = patternAdded;
    }
    
    public ExpressionPattern[] getPatterns() {
    	return this.patterns;
    }
    
    public void setPatterns(ExpressionPattern[] patterns) {
    	this.patterns = patterns;
    }
    
    public boolean isAddingPattern() {
    	return this.addingPattern;
    }
    
    public boolean getNoPatternCheckedForDeletion() {
    	return this.noPatternCheckedForDeletion;
    }
    
    // added by xingjun - 06/06/2008
    public void setNoPatternCheckedForDeletion(boolean noPatternCheckedForDeletion) {
    	this.noPatternCheckedForDeletion = noPatternCheckedForDeletion;
    }

    // xingjun - 21/07/2011 - expression might be null
    public String getNotes() {
//    	return this.expression.getExpressionNote();
    	String notes = null;
    	if (this.expression != null) {
    		notes = this.expression.getExpressionNote();
    	}
    	return notes;
    }
    
    public void setNotes(String notes) {
    	this.expression.setExpressionNote(notes);
    }
    
//    public boolean isAnyPatternSelected() {
//    	return this.anyPatternSelected;
//    }
//    
//    public void setAnyPatternSelected(boolean anyPatternSelected) {
//    	this.anyPatternSelected = anyPatternSelected;
//    }
    
    public String getSubmissionId() {
    	return this.submissionId;
    }
    
    public void setSubmissionId(String submissionId) {
    	this.submissionId = submissionId;
    }
    
    public String getComponentId() {
    	return this.componentId;
    }
    
    public void setComponentId(String componentId) {
    	this.componentId = componentId;
    }
    
    public int getErrorCode() {
    	return this.errorCode;
    }
    
    public void setErrorCode(int errorCode) {
    	this.errorCode = errorCode;
    }
    
    public boolean getExpressionUpdated() {
    	return this.expressionUpdated;
    }
    
    public void setExpressionUpdated(boolean expressionUpdated) {
    	this.expressionUpdated = expressionUpdated;
    }
    
    public boolean isUpdateButtonClicked() {
    	return this.updateButtonClicked;
    }
    
    public void setUpdateButtonClicked(boolean updateButtonClicked) {
    	this.updateButtonClicked = updateButtonClicked;
    }
    
    public boolean isSubmissionLocked() {
    	return this.submissionLocked;
    }
    
    public void setSubmissionLocked(boolean submissionLocked) {
    	this.submissionLocked = submissionLocked;
    }
    
    public boolean isUnlocking() {
    	return this.unlocking;
    }
    
    public void setUnlocking(boolean unlocking) {
    	this.unlocking = unlocking;
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
    
    public String getUserName() {
    	return this.userName;
    }
    
    public void setUserName(String userName) {
    	this.userName = userName;
    }
    
    public User getUser() {
    	return this.user;
    }
    
    public void setUser(User user) {
    	this.user = user;
    }
    
    
    /**
     * 
     * @return
     */
    public String addPattern() {
    	String primaryStrength = FacesUtil.getRequestParamValue("primaryStrength");
//    	if (this.updateButtonClicked == true) { // added by xingjun - 18/06/2008
//    		return "editExpression";
//    	}
    	
    	// check strength. can only add pattern when the strength value is 'present'
    	// modified by xingjun - 25/06/2008 - can add pattern if the expression is 'uncertain' as well
    	if (primaryStrength.equals("present") || primaryStrength.equals("uncertain")) {
        	// only execute when there's no patter is being added
            String addingPattern = (String)FacesUtil.getSessionValue("addingPattern");
//            System.out.println("addPattern value: " + addingPattern);
            
            if (addingPattern == null || addingPattern.equals("false")) {
            	if (this.patterns == null) { // there's no linked pattern info; need to add one
            		patterns = new ExpressionPattern[1];
            		patterns[0] = new ExpressionPattern();
            	} else { // add one extra pattern record into current pattern array
            		ExpressionPattern[] currentPatterns = patterns;
            		int len = currentPatterns.length;
            		ExpressionPattern[] newPatterns =  new ExpressionPattern[len + 1];
                	for (int i=0;i<len;i++) {
                		newPatterns[i] = currentPatterns[i];
                	}
                	newPatterns[len] = new ExpressionPattern();
                	this.patterns = newPatterns;

                	this.noPatternCheckedForDeletion = false;
            	}
            	// set session value: adding pattern
                FacesUtil.setSessionValue("addingPattern", "true");
            } // end of checking session value 'addingPattern'
       }
    	return "editExpression";
    }
    
    /**
     * 
     * @return
     */
    public String deletePattern() {
    	String primaryStrength = FacesUtil.getRequestParamValue("primaryStrength");
//    	if (this.updateButtonClicked == true) { // added by xingjun - 18/06/2008
//    		return "editExpression";
//    	}

    	// check strength. can only delete pattern when the strength value is 'present'
    	if (primaryStrength.equals("present") || primaryStrength.equals("uncertain")) {
    		this.noPatternCheckedForDeletion = false;
    		
    		if (this.patterns == null) { // cannot delete if there's no pattern info
    			this.noPatternCheckedForDeletion = false;
    		} else {
    			// check how many patterns have been checked;
    			int numberOfCheckedPattern = this.getNumberOfPatternsMarkedForDeletion(this.patterns);
    			if (numberOfCheckedPattern < 1) {
    				this.noPatternCheckedForDeletion = true;
    			} else { // restructure the patterns
            		ExpressionPattern[] currentPatterns = patterns;
            		int len = currentPatterns.length;
            		if (len == numberOfCheckedPattern) { // delete all patterns
            			this.patterns = null;
            			this.noPatternCheckedForDeletion = false;

            			// set session value: adding pattern
                        FacesUtil.setSessionValue("addingPattern", "false");
            		} else { // delete part of patterns
            			int newLen = len - numberOfCheckedPattern;
            			ExpressionPattern[] newPatterns = new ExpressionPattern[newLen];
            			for (int i=0,j=0;i<len;i++) {
            				if (!patterns[i].isSelected()) {
            					newPatterns[j] = patterns[i];
            					j++;
            				}
            			}
            			this.patterns = newPatterns;
            			this.noPatternCheckedForDeletion = false;
            			
            			// if added pattern has been checked, delete it and allow user to add new pattern
            			String patternValue = patterns[newLen - 1].getPattern();
            			if (patternValue != null && !patternValue.equals("")) {
//                			System.out.println("newLen: " + newLen);
//                			System.out.println("pattern: " + patterns[newLen - 1].getPattern());
                			// set session value: adding pattern
                            FacesUtil.setSessionValue("addingPattern", "false");
            			}
            		}
    			} // end of restructure pattern after deletion
    		} // end of check pattern existing
    	} // end of check strengrh
    	return "editExpression";
    }
    
    /**
     * cancel all modification made by user
     * @return
     */
    public String cancelModification() {
        // get expression detail
//     	String submissionId = this.expression.getSubmissionId();
//         String componentId = this.expression.getComponentId();
//     	this.expression = editExpressionAssembler.getExpression(submissionId, componentId);
    	String submissionId = FacesUtil.getRequestParamValue("submissionId");
        String componentId = FacesUtil.getRequestParamValue("componentId");
    	this.expression = editExpressionAssembler.getExpression(submissionId, componentId);

        // get pattern infomation
        if (expression.isAnnotated()) {
          	this.patterns = editExpressionAssembler.getPatterns(String.valueOf(expression.getExpressionId()));
        } else {
        	this.patterns = null;
        }
        // reset
        FacesUtil.setSessionValue("addingPattern", "false");
        this.noPatternCheckedForDeletion = false;
//        this.updateButtonClicked = false;
        this.errorCode = 0;
    	return "editExpression";
    }
    
    /**
     * if user change strength from 'present'/'uncertain' to other, the pattern value should be set to blank
     * @param value
     * @return
     */
    public String changeStrength(ValueChangeEvent value) {
    	String strengthValue = (String)value.getNewValue();
    	if (!strengthValue.equals("present") && !strengthValue.equals("uncertain")) {
    		this.patterns = null;
            FacesUtil.setSessionValue("addingPattern", "false");
    		this.noPatternCheckedForDeletion = false;
    	}
    	return null;
    }
    
    /**
     * @author xingjun - 02/03/2009
     * @param value
     * @return
     */
    public String changePattern(ValueChangeEvent value) {
    	String patternValue = (String)value.getNewValue();
    	if (patternValue.equals("") ) {
    		// when user change pattern into "",
    		// location value should be set to ""
    	}
    	return null;
    }
    
    /**
     * if user change location from 'adjacent to xxxxx', 
     * the numerical part of it should be set to blank
     * @param value
     * @return
     */
    public String changeLocation(ValueChangeEvent value) {
    	String locationValue = (String)value.getNewValue();
    	if (!locationValue.equals("adjacent to")) {
    		// do nothing, wait for application refresh the page
    	}
    	return null;
    }
    
    /**
     * 
     * @param patterns
     * @return
     */
//    private boolean getAnyPatternsCheckedForDeletion(ExpressionPattern[] patterns) {
//    	for (int i=0;i<patterns.length;i++) {
//    		ExpressionPattern pattern = patterns[i];
//    		if (pattern.isSelected()) {
//    			return true;
//    		}
//    	}
//    	return false;
//    }
    
    /**
     * 
     * @param patterns
     * @return
     */
    private int getNumberOfPatternsMarkedForDeletion(ExpressionPattern[] patterns) {
    	int count = 0;
    	for (int i=0;i<patterns.length;i++) {
    		ExpressionPattern pattern = patterns[i];
    		if (pattern.isSelected()) {
    			count ++;
    		}
    	}
    	return count;
    }
    
    /**
     * @author xingjun
     * modified by ying
     * @return
     */
    public String updateExpression() {
    	// cannot edit if not logged in
    	if (this.userName == null) return null;
    	
    	// update button clicked
//    	this.updateButtonClicked = true;
    	
    	// get expression detail from database
    	String submissionId = FacesUtil.getRequestParamValue("submissionId");
        String componentId = FacesUtil.getRequestParamValue("componentId");
//        String submissionId = this.expression.getSubmissionId();
//        String componentId = this.expression.getComponentId();
//        System.out.println("VIEW ID:"+FacesContext.getCurrentInstance().getViewRoot().getViewId());
        // modified by xingjun - 04/08/2008 - pertain to locking function
//      modified by ying 20080909
        if (!FacesContext.getCurrentInstance().getViewRoot().getViewId().equalsIgnoreCase("/pages/ish_edit_pattern.jsp") && !this.lockSubmission(submissionId)) {
        	return "editExpression";
        }

    	//edited by Ying
        Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("userBean");
        int privilege = 0;
        if (object != null) {
        	UserBean ub = (UserBean)object;
        	if(null != ub.getUser())
        		privilege = ub.getUser().getUserPrivilege();
        }
        if(privilege >= 3 && FacesContext.getCurrentInstance().getViewRoot().getViewId().equalsIgnoreCase("/pages/edit_expression.jsp")) {
        	if(privilege == 3) {
        		editExpressionAssembler.signOffAnnotation(submissionId, "19");
        	} else if(privilege == 4) {
        		editExpressionAssembler.signOffAnnotation(submissionId, "21");
        	} else if(privilege == 5) {
        		editExpressionAssembler.signOffAnnotation(submissionId, "23");
        	} else if(privilege >= 6) {
        		editExpressionAssembler.signOffAnnotation(submissionId, "25");
        	} 
        }        
        
//        System.out.println("submissionId: " + submissionId);
//        System.out.println("componentId" + componentId);
        ExpressionDetail expressionInfoInDB = editExpressionAssembler.getExpression(submissionId, componentId);

        // get pattern detail from database
        ExpressionPattern[] patternInfoInDB = null;
        if (expressionInfoInDB.isAnnotated()) {
//        	System.out.println("annotated#######");
//        	System.out.println("ExpressionId: " + expression.getExpressionId());
//        	System.out.println("ExpressionIdInDB: " + expressionInfoInDB.getExpressionId());
        	patternInfoInDB = editExpressionAssembler.getPatterns(String.valueOf(expressionInfoInDB.getExpressionId()));
//        	if (patternInfoInDB == null) System.out.println("patternInfoInDB is null though#######");
        } else {
//        	System.out.println("not annotated#######");
        	patternInfoInDB = null;
        }
        String notesInDB = expressionInfoInDB.getExpressionNote();
        String notesOnPage = this.expression.getExpressionNote();
        
        /** strength and pattern */
        if (!expressionInfoInDB.isAnnotated()) { // not annotated in database
        	if (this.expression.getPrimaryStrength() != null
        			&& !this.expression.getPrimaryStrength().equals("")) { // annotated on page
        		// update primary strength in database to 'present'
        		// update additional strength to db if there's a value
        		// log it
//    			expressionUpdated =
//    				editExpressionAssembler.addAnnotation(submissionId, componentId, this.expression.getStage(),
//    						this.expression.getPrimaryStrength(),
//    						this.expression.getSecondaryStrength(), this.patterns, userName);
//    			if (expressionUpdated == false) { // updating failed, reset the strength value
//    				this.expression.setPrimaryStrength(expressionInfoInDB.getPrimaryStrength());
//    			}
        		/*System.out.println("Add annotation:"+submissionId+":"+componentId+":"+this.expression.getStage()+":"+
						this.expression.getPrimaryStrength()+":"+
						this.expression.getSecondaryStrength()+":"+ this.patterns+":"+userName);*/
    			errorCode =
    				editExpressionAssembler.addAnnotation(submissionId, componentId, this.expression.getStage(),
    						this.expression.getPrimaryStrength(),
    						this.expression.getSecondaryStrength(), this.patterns, userName);
    			if (errorCode != 0) { // updating failed, reset the strength value
//    				this.expression.setPrimaryStrength(expressionInfoInDB.getPrimaryStrength());
    				return "editExpression";
    			}
        	}
        } else { // annotated in database
        	if (this.expression.getPrimaryStrength() != null &&
        			!this.expression.getPrimaryStrength().equals("")) { // expressed on page
        		// update secondary strength as appropriate
        		// update pattern info
        		// log it
//        		expressionUpdated =
//        			editExpressionAssembler.updateAnnotation(submissionId, componentId, this.expression.getStage(),
//        					this.expression.getPrimaryStrength(), expressionInfoInDB.getPrimaryStrength(),
//        					this.expression.getSecondaryStrength(), expressionInfoInDB.getSecondaryStrength(),
//        					this.patterns, patternInfoInDB, userName);
        		/*System.out.println("Update annotation:"+submissionId+":"+componentId+":"+this.expression.getStage()+":"+
    					this.expression.getPrimaryStrength()+":"+ expressionInfoInDB.getPrimaryStrength()+":"+
    					this.expression.getSecondaryStrength()+":"+ expressionInfoInDB.getSecondaryStrength()+":"+
    					this.patterns+":"+ patternInfoInDB+":"+ userName);*/
//        		if (this.patterns == null || this.patterns.length == 0 
//        				|| this.patterns[0] == null || this.patterns[0].getPattern() == null 
//        				|| this.patterns[0].getPattern().equals("")) {
//        			System.out.println("editExpressionBean@patternOnPage is null");
//        		}
//        		if (patternInfoInDB == null || patternInfoInDB.length == 0 
//        				|| patternInfoInDB[0] == null || patternInfoInDB[0].getPattern() == null 
//        				|| patternInfoInDB[0].getPattern().equals("")) {
//        			System.out.println("editExpressionBean@patternInDB is null");
//        		}
        		errorCode =
        			editExpressionAssembler.updateAnnotation(submissionId, componentId, this.expression.getStage(),
        					this.expression.getPrimaryStrength(), expressionInfoInDB.getPrimaryStrength(),
        					this.expression.getSecondaryStrength(), expressionInfoInDB.getSecondaryStrength(),
        					this.patterns, patternInfoInDB, userName);
        		if (errorCode != 0) {
        			return "editExpression";
        		}
        		
        	} else { // not expressed on page
        		// delete notes in database if there are any
        		// log it
        		if (notesInDB != null && notesInDB.trim().length() > 0) { // modified by xingjun - 28/05/2008 - it's possible there's empty string note exists
            		expressionUpdated =
            			editExpressionAssembler.deleteNote(submissionId, componentId, userName);
            		///// may need to evaluate the success of deleting notes //////
            		if (expressionUpdated) { // after deleting, shouldn't have strength value
            			this.expression.setExpressionNote(null);
            		} else {
            			return "editExpression";
            		}
        		}

        		// update primary strength in database to specified value
        		// update secondary strength in database to ""
        		// delete pattern and/or location from database
        		// log it
        		expressionUpdated =
        			editExpressionAssembler.deleteAnnotation(submissionId, componentId, 
        					patternInfoInDB, userName);
        		if (expressionUpdated) { // after deleting, shouldn't have strength value
        			this.expression.setPrimaryStrength(null);
        			this.expression.setSecondaryStrength(null);
        		} else {
        			return "editExpression";
        		}
        	}
        }
        
        /** expression note */
        // executed when not deleting annotation
        if (notesInDB == null ) {
        	if (notesOnPage != null && notesOnPage.trim().length() > 0) {
        		// insert notes into database
        		// log it
        		expressionUpdated =
        			editExpressionAssembler.addNote(submissionId, componentId, notesOnPage, userName);
        		if (!expressionUpdated) {
        			return "editExpression";
        		}
        	}
        } else { // notesInDB is not null
        	if (notesOnPage == null || notesOnPage.trim().length() < 1) { // modified by Xingjun - 28/05/2008 - when user delete notes by deleting all characters in the note textfield
        		// delete notes in database;
        		// log it
        		expressionUpdated =
        			editExpressionAssembler.deleteNote(submissionId, componentId, userName);
        		if (!expressionUpdated) {
        			return "editExpression";
        		}
        	} else {
        		if (!notesInDB.equals(notesOnPage)) {
        			// update notes in database
        			// log it
            		expressionUpdated =
            			editExpressionAssembler.updateNote(submissionId, componentId, notesOnPage, userName);
            		if (!expressionUpdated) {
            			return "editExpression";
            		}
        		}
        	}
        }
        
        // reset
        FacesUtil.setSessionValue("addingPattern", "false");
        this.noPatternCheckedForDeletion = false;
//        this.updateButtonClicked = false;
        this.errorCode = 0;
        return "editExpression";
    } // end of updateExpression()
    
    
    /**
     * @author xingjun - 24/06/2008
     * @return
     */
    public String updateExpressionNote() {
    	// cannot edit if not logged in
    	if (this.userName == null) return null;
    	
    	// get expression detail from database
    	String submissionId = FacesUtil.getRequestParamValue("submissionId");
        String componentId = FacesUtil.getRequestParamValue("componentId");

        // modified by xingjun - 04/08/2008 - pertain to locking function
        if (!this.lockSubmission(submissionId)) {
        	return "editExpression";
        }
    	
    	//edited by Ying
        Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("userBean");
        int privilege = 0;
        if (object != null) {
        	UserBean ub = (UserBean)object;
        	if(null != ub.getUser())
        		privilege = ub.getUser().getUserPrivilege();
        }
        if(privilege >= 3 && FacesContext.getCurrentInstance().getViewRoot().getViewId().equalsIgnoreCase("/pages/edit_expression.jsp")) {
        	if(privilege == 3) {
        		editExpressionAssembler.signOffAnnotation(submissionId, "19");
        	} else if(privilege == 4) {
        		editExpressionAssembler.signOffAnnotation(submissionId, "21");
        	} else if(privilege == 5) {
        		editExpressionAssembler.signOffAnnotation(submissionId, "23");
        	} else if(privilege >= 6) {
        		editExpressionAssembler.signOffAnnotation(submissionId, "25");
        	} 
        }        
        
//        System.out.println("submissionId: " + submissionId);
//        System.out.println("componentId" + componentId);
        ExpressionDetail expressionInfoInDB = editExpressionAssembler.getExpression(submissionId, componentId);

        String notesInDB = expressionInfoInDB.getExpressionNote();
        String notesOnPage = this.expression.getExpressionNote();
        
        /** expression note */
        // executed when not deleting annotation
        if (notesInDB == null ) {
        	if (notesOnPage != null && notesOnPage.trim().length() > 0) {
        		// insert notes into database
        		// log it
        		expressionUpdated =
        			editExpressionAssembler.addNote(submissionId, componentId, notesOnPage, userName);
        		if (!expressionUpdated) {
        			return "editExpression";
        		}
        	}
        } else { // notesInDB is not null
        	if (notesOnPage == null || notesOnPage.trim().length() < 1) { // modified by Xingjun - 28/05/2008 - when user delete notes by deleting all characters in the note textfield
        		// delete notes in database;
        		// log it
        		expressionUpdated =
        			editExpressionAssembler.deleteNote(submissionId, componentId, userName);
        		if (!expressionUpdated) {
        			return "editExpression";
        		}
        	} else {
        		if (!notesInDB.equals(notesOnPage)) {
        			// update notes in database
        			// log it
            		expressionUpdated =
            			editExpressionAssembler.updateNote(submissionId, componentId, notesOnPage, userName);
            		if (!expressionUpdated) {
            			return "editExpression";
            		}
        		}
        	}
        }
        
        // reset
        FacesUtil.setSessionValue("addingPattern", "false");
        this.noPatternCheckedForDeletion = false;
//        this.updateButtonClicked = false;
        this.errorCode = 0;
        return "editExpression";
    }
    
    /**
     * @author xingjun
     * @return
     */
    public String unlockSubmission() {
    	String lockingSwitchOn = Utility.getLockingSwitchOn();
    	if (lockingSwitchOn.equals("false")) { // locking function switched on
    		return "editExpression";
    	}
//    	System.out.println("---------eeBean:unlockSubmission###");
//    	if (this.submissionLocked == false) {
//    		return "editExpression";
//    	}
    	String submissionId = FacesUtil.getRequestParamValue("submissionId");
//    	String submissionId = this.expression.getSubmissionId();
//    	System.out.println("unlockSubmission:submissionId: " + submissionId);
		int privilege = Utility.getUserPriviledges();
		if (privilege < 5) {
			this.errorCode = 5;
			return "editExpression";
		}
		int submissionUnlocked =
			EditAssemblerUtil.unlockSubmission(submissionId);
		if (submissionUnlocked == 0) {
			this.errorCode = 7;
		} else {
			this.errorCode = 0;
//			this.submissionLocked = false;
			this.lockingText = "UNLOCKED";
			this.lockingTextDisplayStyle = Utility.getUnlockedTextDisplayStyle();
			this.unlocking = true;
		}
//		System.out.println("###eeBean:errorCode: " + errorCode);
    	return "editExpression";
    }
    
    /**
     * @author ying
     * @return
     */
    public String annotationSignOff() {
    	String submissionId = null;
    	if(null != getRequestSubmissionId()) {
    		submissionId = getRequestSubmissionId();
    	} else {
    		submissionId = getSubmissionId();
    	}
//    	System.out.println("signoff:"+submissionId);

    	// modified by xingjun - 04/08/2008 - pertain to locking function
        if (!this.lockSubmission(submissionId)) {
        	return "LabIshEdit";
        }
    	
		int privilege = Utility.getUserPriviledges();
        if(privilege == 3) {
        	editExpressionAssembler.signOffAnnotation(submissionId, "20");
        } else if(privilege == 4) {
        	editExpressionAssembler.signOffAnnotation(submissionId, "22");
        } else if(privilege == 5) {
        	editExpressionAssembler.signOffAnnotation(submissionId, "24");
        } else if(privilege >= 6) {
        	editExpressionAssembler.signOffAnnotationAndSetPublicSubmission(submissionId, "4", "1");
        	// set sub_is_public=1
        	//editExpressionAssembler.setPublicSubmission(submissionId, "1");
        } 
        
    	String lockingSwitchOn = Utility.getLockingSwitchOn();
    	if (lockingSwitchOn.equals("false")) { // locking function switched on
    		return "LabIshEdit";
    	}

		if (privilege < 3) {
			this.errorCode = 5;
			return "LabIshEdit";
		}
		int submissionUnlocked =
			EditAssemblerUtil.unlockSubmission(submissionId);
		if (submissionUnlocked == 0) {
			this.errorCode = 7;
		} else {
			this.errorCode = 0;
//			this.submissionLocked = false;
			this.lockingText = "UNLOCKED";
			this.lockingTextDisplayStyle = Utility.getUnlockedTextDisplayStyle();
			this.unlocking = true;
		}        
        
        
		return "LabIshEdit";
    } // end of annotationSignOff
    
    /**
     * @author Ying
     * @return
     */
    public String annotationReturnToAnnotator() {
    	String submissionId = null;
    	if(null != getRequestSubmissionId()) {
    		submissionId = getRequestSubmissionId();
    	} else {
    		submissionId = getSubmissionId();
    	}
//    	System.out.println("returntoannot:"+submissionId);
    	/*ArrayList list = editExpressionAssembler.getPIBySubID(submissionId);
    	if (list != null && list.size() == 4) {
        	//System.out.println("returntoannot:"+list.get(0)+":"+list.get(1)+":"+list.get(2)+":"+list.get(3));
			FacesUtil.setFacesRequestParamValue("labId",list.get(0));
			FacesUtil.setFacesRequestParamValue("date",list.get(1));
			FacesUtil.setFacesRequestParamValue("assayType", list.get(2));
			FacesUtil.setFacesRequestParamValue("isPublic",list.get(3));
        }*/
    	
        // modified by xingjun - 04/08/2008 - pertain to locking function
        if (!this.lockSubmission(submissionId)) {
        	return "LabIshEdit";
        }
        
        editExpressionAssembler.signOffAnnotationAndSetPublicSubmission(submissionId, "5", "0");
        
    	String lockingSwitchOn = Utility.getLockingSwitchOn();
    	if (lockingSwitchOn.equals("false")) { // locking function switched on
    		return "LabIshEdit";
    	}

		int privilege = Utility.getUserPriviledges();
		if (privilege < 4) {
			this.errorCode = 5;
			return "LabIshEdit";
		}
		int submissionUnlocked =
			EditAssemblerUtil.unlockSubmission(submissionId);
		if (submissionUnlocked == 0) {
			this.errorCode = 7;
		} else {
			this.errorCode = 0;
//			this.submissionLocked = false;
			this.lockingText = "UNLOCKED";
			this.lockingTextDisplayStyle = Utility.getUnlockedTextDisplayStyle();
			this.unlocking = true;
		}         
        
        
        return "LabIshEdit";
    }    
    
    /**
     * @author ying
     * @return
     */
    public String annotationReturnToEditor() {
    	String submissionId = null;
    	if(null != getRequestSubmissionId()) {
    		submissionId = getRequestSubmissionId();
    	} else {
    		submissionId = getSubmissionId();
    	}
//    	System.out.println("returntoEdit:"+submissionId);
    	/*ArrayList list = editExpressionAssembler.getPIBySubID(submissionId);
        if (list != null && list.size() == 4) {
        	//System.out.println("returntoEdit:"+list.get(0)+":"+list.get(1)+":"+list.get(2)+":"+list.get(3));
			FacesUtil.setFacesRequestParamValue("labId",list.get(0));
			FacesUtil.setFacesRequestParamValue("date",list.get(1));
			FacesUtil.setFacesRequestParamValue("assayType", list.get(2));
			FacesUtil.setFacesRequestParamValue("isPublic",list.get(3));
        }*/
    	
        // modified by xingjun - 04/08/2008 - pertain to locking function
        if (!this.lockSubmission(submissionId)) {
        	return "LabIshEdit";
        }

        editExpressionAssembler.signOffAnnotationAndSetPublicSubmission(submissionId, "22", "0");
        
    	String lockingSwitchOn = Utility.getLockingSwitchOn();
    	if (lockingSwitchOn.equals("false")) { // locking function switched on
    		return "LabIshEdit";
    	}

		int privilege = Utility.getUserPriviledges();
		if (privilege < 6) {
			this.errorCode = 5;
			return "LabIshEdit";
		}
		int submissionUnlocked =
			EditAssemblerUtil.unlockSubmission(submissionId);
		if (submissionUnlocked == 0) {
			this.errorCode = 7;
		} else {
			this.errorCode = 0;
//			this.submissionLocked = false;
			this.lockingText = "UNLOCKED";
			this.lockingTextDisplayStyle = Utility.getUnlockedTextDisplayStyle();
			this.unlocking = true;
		} 
        
        // set sub_is_public=1
        //editExpressionAssembler.setPublicSubmission(submissionId, "0");
        return "LabIshEdit";
    }
    
	private String getRequestSubmissionId() {
		ISHSingleSubmissionBean ub =
			(ISHSingleSubmissionBean)(FacesUtil.getFacesRequestParamObject("ishSubmissionBean"));
		if (ub != null)
			return ub.getSubmission().getAccID();
		return null;
	}
	
	/**
	 * @author xingjun - 04/08/2008
	 * @return
	 */
	public boolean lockSubmission(String submissionId) {
//		System.out.println("editExpressionBean:lockSubmission@submissionId: " + submissionId);
		String lockingSwitchOn = Utility.getLockingSwitchOn();
        if (lockingSwitchOn.equals("true")) { // locking function switch on
            // check locking info - do nothing if locked, lock the submission if unlocked
        	this.lockingInfo = EditAssemblerUtil.getLockingInfo(submissionId);
        	if (this.lockingInfo != null) {
        		String lb = this.lockingInfo.getLockedBy();
        		int lockTime = this.lockingInfo.getLockTime();
        		int definedLockTimeOut = Integer.parseInt(Utility.getLockTimeOut());
        		// might be locked by others
        		if (!this.userName.equals(lb) 
        				&& (lockTime-definedLockTimeOut < 0 || lockTime-definedLockTimeOut == 0)) { // locked
//        			this.submissionLocked =  true;
        			this.lockingText = "LOCKED by " + lb + ". Cannot modify";
        			this.lockingTextDisplayStyle = Utility.getLockedTextDisplayStyle();
        			return false;
        		} else { // re-lock - update lock time;
//        			int submissionLocked =
//        				EditAssemblerUtil.relockSubmission(submissionId, this.userName);
        			int submissionLocked =
        				EditAssemblerUtil.relockSubmission(submissionId, this.user);
        			if (submissionLocked == 0) { // failed to re-lock
        				this.errorCode = 7;
        				return false;
        			}
        		}
        	} else { // unlocked - need to lock
//        		System.out.println("lock submission by eebean: update expression!");
//    			int submissionLocked =
//    				EditAssemblerUtil.lockSubmission(submissionId, this.userName);
    			int submissionLocked =
    				EditAssemblerUtil.lockSubmission(submissionId, this.user);
    			if (submissionLocked == 0) { // failed to lock
    				this.errorCode = 7;
    				return false;
    			}
        	}
    		this.errorCode = 0;
//    		this.submissionLocked =  false;
    		this.lockingText = "LOCKED by " + this.userName;
    		this.lockingTextDisplayStyle = Utility.getLockedTextDisplayStyle();
        }
        // successful
        return true;
	}
	
	/**
	 * @author xingjun - 09/03/2010
	 * @param userType
	 * @param userPrivilege
	 * @param submissionDbStatus
	 * @return
	 */
	public boolean isUserAccessPermitted(String userType, int userPrivilege, int submissionDbStatus) {
		boolean result = true;
        Object object = FacesUtil.getSessionValue("userBean");
        if (object != null) {
    		if (userType.equals("EXAMINER")) {
    			result = false;
    		} else if (userPrivilege == 3 && (submissionDbStatus==3 || submissionDbStatus==4 || submissionDbStatus>19)) { 
    			result = false;
    		} else if (userPrivilege==4 && (submissionDbStatus==3 || submissionDbStatus==4 || submissionDbStatus>21)) {
    			result = false;
    		} else if (userPrivilege==5 && (submissionDbStatus==3 || submissionDbStatus>23)) {
    			result = false;
    		}
        }
		return result;
	}
	
	/**
	 * @author xingjun - 09/03/2010
	 * @return
	 */
	public boolean isUserAccessPermitted() {
		boolean result = true;
        Object object = FacesUtil.getSessionValue("userBean");
        if (object != null) {
        	UserBean ub = (UserBean)object;
        	String userType = ub.getUser().getUserType();
        	int userPrivilege = ub.getUser().getUserPrivilege();
        	int submissionDbStatus = this.expression.getSubmissionDbStatus();
    		if (userType.equals("EXAMINER")) {
    			result = false;
    		} else if (userPrivilege == 3 && (submissionDbStatus==3 || submissionDbStatus==4 || submissionDbStatus>19)) { 
    			result = false;
    		} else if (userPrivilege==4 && (submissionDbStatus==3 || submissionDbStatus==4 || submissionDbStatus>21)) {
    			result = false;
    		} else if (userPrivilege==5 && (submissionDbStatus==3 || submissionDbStatus>23)) {
    			result = false;
    		}
        }
		return result;
	}
	
	/**
	 * @author xingjun - 09/03/2010
	 * @return
	 */
	public boolean isStrengthSelectionRendered() {
		boolean rendered = false;
    	String expressionValue = this.expression.getPrimaryStrength();
    	if (expressionValue != null 
    			&& !expressionValue.equals("")
    			&& expressionValue.equals("present")) {
    		rendered = true;
    	}
		return rendered;
	}
	
	/**
	 * @author xingjun - 09/03/2010
	 * @return
	 */
	public boolean isStrengthSelectionDisabled() {
//		System.out.println("EditExpressionBean:strengthSelectionDisabled#####");
		boolean disabled = false;
        Object object = FacesUtil.getSessionValue("userBean");
        if (object != null) {
        	UserBean ub = (UserBean)object;
        	String userType = ub.getUser().getUserType();
        	String expressionValue = this.expression.getPrimaryStrength();
        	int userPrivilege = ub.getUser().getUserPrivilege();
        	int submissionDbStatus = this.expression.getSubmissionDbStatus();
//        	System.out.println("EditExpressionBean:strengthSelectionDisabled:expression: " + expressionValue);
        	if (!this.isUserAccessPermitted(userType, userPrivilege, submissionDbStatus)) {
        		disabled = true;
    		} else if (expressionValue == null 
    				|| expressionValue.equals("") 
    				|| expressionValue.equals("not examined")
    				|| expressionValue.equals("not detected")) {
    			disabled = true;
    		}
        }
		return disabled;
	}
	
	/**
	 * @author xingjun 09/03/2010
	 * @return
	 */
	public boolean isPatternSelectionRendered() {
		boolean rendered = false;
    	String expressionValue = this.expression.getPrimaryStrength();
    	if (expressionValue != null && !expressionValue.equals("") 
			&& (expressionValue.equals("present") || expressionValue.equals("uncertain"))) {
    		rendered = true;
    	}
		return rendered;
	}
	
	//////////////////////// for time being not use
	public boolean isLocationSelectionRendered() {
		boolean rendered = false;
    	String expressionValue = this.expression.getPrimaryStrength();
    	if (expressionValue != null && !expressionValue.equals("") 
			&& (expressionValue.equals("present") || expressionValue.equals("uncertain"))) {
    		rendered = true;
    	}
		return rendered;
//		pattern.pattern != null 
//		&& pattern.pattern != '' && pattern.locationAPart == 'adjacent to'
	}
	
	/**
	 * @author xingjun - 08/03/2010
	 * @return
	 */
	public boolean isExpressionNoteDisabled() {
		boolean disabled = false;
        Object object = FacesUtil.getSessionValue("userBean");
        if (object != null) {
        	UserBean ub = (UserBean)object;
        	String userType = ub.getUser().getUserType();
        	String expressionValue = this.expression.getPrimaryStrength();
        	int userPrivilege = ub.getUser().getUserPrivilege();
        	int submissionDbStatus = this.expression.getSubmissionDbStatus();
        	if (!this.isUserAccessPermitted(userType, userPrivilege, submissionDbStatus)) {
        		disabled = true;
    		} else if (expressionValue == null || expressionValue.equals("") || expressionValue.equals("not examined")) {
    			disabled = true;
    		}
//            System.out.println("EditExpressionBean:isExpressionNoteDisabled:expressionValue " + expressionValue);
        }
//        System.out.println("EditExpressionBean:isExpressionNoteDisabled: " + disabled);
		return disabled;
	}
	
	/**
	 * @author xingjun - 09/03/2010
	 * Apply to 'delete pattern', 'add pattern', 'cancel modification', 
	 * and 'update' button
	 * @return
	 */
	public boolean isExpressionEditButtonRendered() {
		boolean rendered = false;
        Object object = FacesUtil.getSessionValue("userBean");
        if (object != null) {
        	UserBean ub = (UserBean)object;
        	boolean userLoggedIn = ub.isUserLoggedIn();
        	String userType = ub.getUser().getUserType();
        	int userPrivilege = ub.getUser().getUserPrivilege();
        	int submissionDbStatus = this.expression.getSubmissionDbStatus();
        	if (userLoggedIn && !userType.equals("EXAMINER")) {
        		if (userPrivilege > 5) {
        			rendered = true;
        		} else if (submissionDbStatus == 2) {
        			rendered = true;
        		} else if (userPrivilege == 3 && submissionDbStatus > 4 && submissionDbStatus <= 19) {
        			rendered = true;
        		} else if (userPrivilege == 4 && submissionDbStatus > 4 && submissionDbStatus <= 21) {
        			rendered = true;
        		} else if (userPrivilege == 5 && submissionDbStatus >= 4 && submissionDbStatus <= 23) {
        			rendered = true;
        		}
        	}
        }
		return rendered;
	}
	
	/**
	 * @author xingjun - 09/03/2010
	 * @return
	 */
	public boolean isUnlockButtonRendered() {
		boolean rendered = false;
        Object object = FacesUtil.getSessionValue("userBean");
        if (object != null) {
        	UserBean ub = (UserBean)object;
        	boolean userLoggedIn = ub.isUserLoggedIn();
        	String userType = ub.getUser().getUserType();
        	int userPrivilege = ub.getUser().getUserPrivilege();
        	int submissionDbStatus = this.expression.getSubmissionDbStatus();
        	if (userLoggedIn && !userType.equals("EXAMINER")) {
        		if (userPrivilege > 5) {
        			rendered = true;
        		} else if (submissionDbStatus == 2) {
        			rendered = true;
        		} else if (userPrivilege == 5 && submissionDbStatus >= 4 && submissionDbStatus <= 23) {
        			rendered = true;
        		}
        	}
        }
		return rendered;
	}
}
