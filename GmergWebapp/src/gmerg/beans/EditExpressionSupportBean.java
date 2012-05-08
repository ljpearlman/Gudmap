/**
 * 
 */
package gmerg.beans;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import gmerg.assemblers.EditExpressionAssembler;
import gmerg.assemblers.EditAssemblerUtil;
import gmerg.entities.submission.ExpressionDetail;
import gmerg.entities.submission.ExpressionPattern;
import gmerg.entities.submission.LockingInfo;
import gmerg.entities.User;

import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;
//import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public class EditExpressionSupportBean {
	private ExpressionDetail expression;
    private EditExpressionAssembler editExpressionAssembler;
    private ExpressionPattern[] patterns;
    private LockingInfo lockingInfo;
    private String userName;
    // added by xingjun - 15/08/2008
    // also changed code related to locking function - they now use user instead of userName
    private User user;
    //////////////////////////
//    private boolean addingPattern;
    private boolean noPatternCheckedForDeletion;
    private EditExpressionBean eeBean;

    /**
     * constructor
     * 
     * Check the parameters submission id & component id:
     * If passed in, page is invoked by clicking on the anatomy tree at the submission detail page OR
     *    clicking on command link on editing page
     *    
     * If not, page is invoked by changing 'strength' value on the editing page OR
     *    changing location value on the editing page
     *    
     * <p>modified by xingjun - 14/05/2008 
     * - when user switched node, compId of eeBean should be updated</p>
     */
    public EditExpressionSupportBean() {
//    	System.out.println("----------initialise editExpressionSupportBean!!!!!----");
        String submissionIdParam = null;
        String componentIdParam = null;
    	String subIdOfSession = null;
    	String compIdOfSession = null;
    	this.eeBean = null;
        this.editExpressionAssembler = new EditExpressionAssembler();
    	
    	/** get parameters */
        submissionIdParam = FacesUtil.getRequestParamValue("submissionId");
        componentIdParam = FacesUtil.getRequestParamValue("componentId");
//    	System.out.println("supportBean parm.subId: " + submissionIdParam);
//    	System.out.println("supportBean parm.compId: " + componentIdParam);

    	/** get EditExpressionBean and submission and component ids in session */
//      Object object = FacesUtil.getSessionValue("editExpressionBean");
        Object object =
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("editExpressionBean");
        if (object != null) {
        	this.eeBean = (EditExpressionBean) object;
        	ExpressionDetail expressionDetail = this.eeBean.getExpressionDetail();
        	if(null != expressionDetail) {
	        	subIdOfSession = this.eeBean.getExpressionDetail().getSubmissionId();
	        	compIdOfSession = this.eeBean.getExpressionDetail().getComponentId();
        	} else {
        		subIdOfSession = this.eeBean.getSubmissionId();
	        	compIdOfSession = this.eeBean.getComponentId();
        	}
        } else { // initialise an eeBean to store necessary information
        	this.eeBean = new EditExpressionBean();
        }
//    	System.out.println("supportBean session.subId: " + subIdOfSession);
//    	System.out.println("supportBean session.compId: " + compIdOfSession);

        // if the parameters are not passed in, go to EditExpressionBean to find the ids
        boolean paramsPassedIn = true;
        if (submissionIdParam == null || submissionIdParam.equals("")
        		|| componentIdParam == null || componentIdParam.equals("")) {
//        	System.out.println("sub or com id is null");
            paramsPassedIn = false;
            submissionIdParam = subIdOfSession;
            componentIdParam = compIdOfSession;
        }
//    	System.out.println("supportBean subIdParam: " + submissionIdParam);
//    	System.out.println("supportBean compIdParam: " + componentIdParam);

    	// get expression detail from db for user asked node
        this.expression = 
        	editExpressionAssembler.getExpression(submissionIdParam, componentIdParam);
        
        if (this.expression != null) {
//        	System.out.println("EditExpressionSupportBean:expression is not null");
            // get pattern infomation
            if (this.expression.isAnnotated()) {
//            	System.out.println("expressed##################" + expression.getExpressionId());
            	this.patterns = 
            		editExpressionAssembler.getPatterns(String.valueOf(this.expression.getExpressionId()));
            }

        	/** compare two sets of ids only if EditExpressionBean has been initialised */
        	if (subIdOfSession != null && !subIdOfSession.equals("") 
        			&& compIdOfSession != null && !compIdOfSession.equals("")) {
        		/** if user clicked on different component node, reset current editExpressionBean */
        		if (!subIdOfSession.equals(submissionIdParam) 
        				|| !compIdOfSession.equals(componentIdParam)) {
//           		System.out.println("user switched to different node");
                	this.eeBean.setExpressionDetail(this.expression);
                	this.eeBean.setPatterns(this.patterns);
                	this.eeBean.setSubmissionId(submissionIdParam);
                	this.eeBean.setComponentId(componentIdParam);
//                	System.out.println("supportBean session.subId after switch: " + this.eeBean.getSubmissionId());
//                	System.out.println("supportBean session.compId after switch: " + this.eeBean.getComponentId());
                	
    		        // reset the parameters -- added by xingjun - 06/06/2008
                	// if user diverted to other submissions/nodes, parameters should be reset
                    FacesUtil.setSessionValue("addingPattern", "false");
    		        this.eeBean.setNoPatternCheckedForDeletion(false);
    		        this.eeBean.setErrorCode(0);
                } else { // clicked on the same node
            		// if the params passed in, it means user clicked on the node in the tree
                	if (paramsPassedIn) {
                		// if expression info on page and in db are not identical, 
                		// reset expression and pattern on page
//                		System.out.println("expressionInDB: " + this.expression.getPrimaryStrength());
//                		System.out.println("expressionOnPage: " + eeBean.getExpressionDetail().getPrimaryStrength());
                		if (this.expression.getPrimaryStrength() != eeBean.getExpressionDetail().getPrimaryStrength()) {
                			this.eeBean.setExpressionDetail(this.expression);
                			this.eeBean.setPatterns(this.patterns);
                		}

                		ExpressionPattern[] patternOnPage = this.eeBean.getPatterns();
                		
                		// if pattern info on page and in db are not identical,
                		// reset the pattern on page 
            			if (this.comparePatterns(patternOnPage, this.patterns) == 0) {
//            				System.out.println("=====update pattern value on page by support bean!!!!!!!!");
                			this.eeBean.setPatterns(this.patterns);
            			}
            			
                        FacesUtil.setSessionValue("addingPattern", "false");
        		        this.eeBean.setNoPatternCheckedForDeletion(false);
        		        this.eeBean.setErrorCode(0);
                	}
                }
            	
        		// get user info
                object = FacesUtil.getSessionValue("userBean");
                if (object != null) {
                	UserBean ub = (UserBean)object;
                	this.user = ub.getUser();
                	this.userName = ub.getUserName();
                } else {
                	this.userName = null;
                }
        		
        		/** locking */
            	String lockingSwitchOn = Utility.getLockingSwitchOn();
            	//modified by ying 20080909
                if (lockingSwitchOn.equals("true") 
                		&& !this.user.getUserType().equals("EXAMINER") // dont lock when examiner looks at the annotation - xingjun - 24/11/2009
                		&& !FacesContext.getCurrentInstance().getViewRoot().
                		getViewId().equalsIgnoreCase("/pages/ish_edit_pattern.jsp")) { // locking function switched on
            		// xingjun - 28/07/2008
                    // get locking info
            		this.lockingInfo = editExpressionAssembler.getLockingInfo(submissionIdParam);
//                    object = FacesUtil.getSessionValue("userBean");
//                    if (object != null) {
//                    	UserBean ub = (UserBean)object;
//                    	this.user = ub.getUser();
//                    	this.userName = ub.getUserName();
//                    } else {
//                    	this.userName = null;
//                    }
//                	System.out.println("locking info obtained in edit expression support bean!");
                	if (this.lockingInfo != null) {
            			this.eeBean.setLockingInfo(lockingInfo);
                		String lb = this.lockingInfo.getLockedBy();
                		int lockTime = this.lockingInfo.getLockTime();
                		int definedLockTimeOut = Integer.parseInt(Utility.getLockTimeOut());
                		if (!this.userName.equals(lb) 
                				&& (lockTime-definedLockTimeOut < 0 || lockTime-definedLockTimeOut == 0)) {
//                			this.eeBean.setSubmissionLocked(true);
                			this.eeBean.setLockingText("LOCKED by " + lb + ". Cannot modify");
                			this.eeBean.setLockingTextDisplayStyle(Utility.getLockedTextDisplayStyle());
                		} else {
                			// update lock time if necessary
                			// if user is just unlocking the submission, dont update
//                			System.out.println("support bean: submission is unlocking?: " + eeBean.isUnlocking());
                			if (!this.eeBean.isUnlocking()) {
//                    			int submissionLocked =
//                    				EditAssemblerUtil.relockSubmission(submissionIdParam, this.userName);
                    			int submissionLocked =
                    				EditAssemblerUtil.relockSubmission(submissionIdParam, this.user);
                    			if (submissionLocked == 0) {
                    				this.eeBean.setErrorCode(7);
                    			} else {
//                                	eeBean.setSubmissionLocked(false);
                        			this.eeBean.setErrorCode(0);
                        			this.eeBean.setLockingText("LOCKED by " + lb);
                        			this.eeBean.setLockingTextDisplayStyle(Utility.getLockedTextDisplayStyle());
                    			}
                    			this.eeBean.setUnlocking(false); // re-set the unlocking
                			}
                		}
                	} else { // unlocked - lock
//                		System.out.println("support bean lockingInfo is null!");
//                		System.out.println("lock submission by support bean!"+submissionIdParam);
//            			int submissionLocked =
//            				EditAssemblerUtil.lockSubmission(submissionIdParam, this.userName);
            			int submissionLocked =
            				EditAssemblerUtil.lockSubmission(submissionIdParam, this.user);
            			if (submissionLocked == 0) {
            				this.eeBean.setErrorCode(7);
            			} else {
            				this.eeBean.setErrorCode(0);
//                        	this.eeBean.setSubmissionLocked(false);
            				this.eeBean.setLockingText("LOCKED by " + this.userName);
            				this.eeBean.setLockingTextDisplayStyle(Utility.getLockedTextDisplayStyle());
            			}
                	}
            	} // end of locking
            } // end of id comparison
        } // end of checking nullability of expression
        // if expression is null, user is not in editor page (edit_expression.jsp) 
        // and EditExpressionBean has not initialised yet
        // user may be in ish_edit_expression page and trying to approve/complete, reject the submission
        else {
//        	System.out.println("EditExpressionSupportBean:expression is null");
            object = FacesUtil.getSessionValue("userBean");
            if (object != null) {
            	UserBean ub = (UserBean)object;
            	this.user = ub.getUser();
            	this.userName = ub.getUserName();
            } else {
            	this.userName = null;
            }
        }
    } // end of constructor
    
//    public EditExpressionSupportBean() {
////    	System.out.println("----------initialise editExpressionSupportBean!!!!!----");
//        String submissionIdParam = null;
//        String componentIdParam = null;
//    	EditExpressionBean eeBean = null;
//    	String subIdOfSession = null;
//    	String compIdOfSession = null;
//        editExpressionAssembler = new EditExpressionAssembler();
//    	
//    	/** get parameters */
//        submissionIdParam = FacesUtil.getRequestParamValue("submissionId");
//        componentIdParam = FacesUtil.getRequestParamValue("componentId");
////    	System.out.println("supportBean parm.subId: " + submissionIdParam);
////    	System.out.println("supportBean parm.compId: " + componentIdParam);
//
//    	/** get EditExpressionBean and submission and component ids in session */
////      Object object = FacesUtil.getSessionValue("editExpressionBean");
//        Object object =
//            FacesContext
//                .getCurrentInstance()
//                    .getExternalContext()
//                        .getSessionMap()
//                            .get("editExpressionBean");
//        if (object != null) {
//        	eeBean = (EditExpressionBean) object;
//        	ExpressionDetail expressionDetail = eeBean.getExpressionDetail();
//        	if(null != expressionDetail) {
//	        	subIdOfSession = eeBean.getExpressionDetail().getSubmissionId();
//	        	compIdOfSession = eeBean.getExpressionDetail().getComponentId();
//        	} else {
//        		subIdOfSession = eeBean.getSubmissionId();
//	        	compIdOfSession = eeBean.getComponentId();
//        	}
//        }
////    	System.out.println("supportBean session.subId: " + subIdOfSession);
////    	System.out.println("supportBean session.compId: " + compIdOfSession);
//
//        // if the parameters are not passed in, go to EditExpressionBean to find the ids
//        boolean paramsPassedIn = true;
//        if (submissionIdParam == null || submissionIdParam.equals("")
//        		|| componentIdParam == null || componentIdParam.equals("")) {
////        	System.out.println("sub or com id is null");
//            submissionIdParam = subIdOfSession;
//            componentIdParam = compIdOfSession;
//            paramsPassedIn = false;
//        }
////    	System.out.println("supportBean subIdParam: " + submissionIdParam);
////    	System.out.println("supportBean compIdParam: " + componentIdParam);
//
//    	// get expression detail from db (for user asked node)
//        this.expression = 
//        	editExpressionAssembler.getExpression(submissionIdParam, componentIdParam);
//        
//        // get pattern infomation
//        if (expression.isAnnotated()) {
////        	System.out.println("expressed##################" + expression.getExpressionId());
//        	patterns = 
//        		editExpressionAssembler.getPatterns(String.valueOf(expression.getExpressionId()));
//        }
//
//    	/** compare two sets of ids only if EditExpressionBean has been initialised */
//    	if (subIdOfSession != null && !subIdOfSession.equals("") 
//    			&& compIdOfSession != null && !compIdOfSession.equals("")) {
//    		/** user clicked on different component node, 
//    		 * need to reset current editExpressionBean */
//    		if (!subIdOfSession.equals(submissionIdParam) 
//    				|| !compIdOfSession.equals(componentIdParam)) {
////       		System.out.println("user switched to different node");
//            	eeBean.setExpressionDetail(this.expression);
//            	eeBean.setPatterns(this.patterns);
//            	
//		        // reset the parameters -- added by xingjun - 06/06/2008
//            	// when user diverted to other submissions,
//            	// the parameter should be reset to their initial values
//                FacesUtil.setSessionValue("addingPattern", "false");
//		        eeBean.setNoPatternCheckedForDeletion(false);
////		        eeBean.setUpdateButtonClicked(false);
//		        eeBean.setErrorCode(0);
//            }
//    		
//    		/** refresh the pattern if necessary */
//    		// modified by xingjun - 02/10/2008 -
//    		// if pattern info on page and in db are not consistent, update the pattern on page 
//    		// modified by xingjun - 27/02/2009 - 
//    		// need check if the params passed in, 
//    		// if yes, it means user clicked on different nodes
//    		// if no, use click on the same node with modification (strength, location, etc.),
//   		    // refresh the page
//    		ExpressionPattern[] patternOnPage = eeBean.getPatterns();
//			if (this.comparePatterns(patternOnPage, this.patterns) == 0 && paramsPassedIn) {
//				System.out.println("=====update pattern value on page by support bean!!!!!!!!");
//            	eeBean.setExpressionDetail(this.expression);
//    			eeBean.setPatterns(this.patterns);
//                FacesUtil.setSessionValue("addingPattern", "false");
//		        eeBean.setNoPatternCheckedForDeletion(false);
//		        eeBean.setErrorCode(0);
//			} else {
//				System.out.println("=====pattern values are the same!!!!!!!!");
//			}
//        	
//            /** locking */
//        	String lockingSwitchOn = Utility.getLockingSwitchOn();
//        	//modified by ying 20080909
//            if (lockingSwitchOn.equals("true") && !FacesContext.getCurrentInstance().getViewRoot().getViewId().equalsIgnoreCase("/pages/ish_edit_pattern.jsp")) { // locking function switched on
//        		// xingjun - 28/07/2008
//                // get locking info
//        		lockingInfo = editExpressionAssembler.getLockingInfo(submissionIdParam);
//                object = FacesUtil.getSessionValue("userBean");
//                if (object != null) {
//                	UserBean ub = (UserBean)object;
//                	this.user = ub.getUser();
//                	this.userName = ub.getUserName();
//                } else {
//                	this.userName = null;
//                }
////            	System.out.println("locking info obtained in edit expression support bean!");
//            	if (this.lockingInfo != null) {
//        			eeBean.setLockingInfo(lockingInfo);
//            		String lb = this.lockingInfo.getLockedBy();
//            		int lockTime = this.lockingInfo.getLockTime();
//            		int definedLockTimeOut = Integer.parseInt(Utility.getLockTimeOut());
//            		if (!this.userName.equals(lb) 
//            				&& (lockTime-definedLockTimeOut < 0 || lockTime-definedLockTimeOut == 0)) {
////            			eeBean.setSubmissionLocked(true);
//            			eeBean.setLockingText("LOCKED by " + lb + ". Cannot modify");
//            			eeBean.setLockingTextDisplayStyle(Utility.getLockedTextDisplayStyle());
//            		} else {
//            			// update lock time if necessary
//            			// if user is just unlocking the submission, dont update
////            			System.out.println("support bean: submission is unlocking?: " + eeBean.isUnlocking());
//            			if (!eeBean.isUnlocking()) {
////                			int submissionLocked =
////                				EditAssemblerUtil.relockSubmission(submissionIdParam, this.userName);
//                			int submissionLocked =
//                				EditAssemblerUtil.relockSubmission(submissionIdParam, this.user);
//                			if (submissionLocked == 0) {
//                				eeBean.setErrorCode(7);
//                			} else {
////                            	eeBean.setSubmissionLocked(false);
//                    			eeBean.setErrorCode(0);
//                    			eeBean.setLockingText("LOCKED by " + lb);
//                    			eeBean.setLockingTextDisplayStyle(Utility.getLockedTextDisplayStyle());
//                			}
//                			eeBean.setUnlocking(false); // re-set the unlocking
//            			}
//            		}
//            	} else { // unlocked - lock
////            		System.out.println("support bean lockingInfo is null!");
////           		System.out.println("XXX lock submission by support bean!"+submissionIdParam);
////        			int submissionLocked =
////        				EditAssemblerUtil.lockSubmission(submissionIdParam, this.userName);
//        			int submissionLocked =
//        				EditAssemblerUtil.lockSubmission(submissionIdParam, this.user);
//        			if (submissionLocked == 0) {
//        				eeBean.setErrorCode(7);
//        			} else {
//            			eeBean.setErrorCode(0);
////                    	eeBean.setSubmissionLocked(false);
//            			eeBean.setLockingText("LOCKED by " + this.userName);
//            			eeBean.setLockingTextDisplayStyle(Utility.getLockedTextDisplayStyle());
//        			}
//            	}
//        	} // locking
//        } // pattern comparison
//    } // end of constructor
    
    public ExpressionDetail getExpressionDetail() {
    	return this.expression;
    }
    
    public void setExpressionDetail(ExpressionDetail expression) {
    	this.expression = expression;
    }
    
    public String getPrimaryStrength() {
    	return this.expression.getPrimaryStrength();
    }
    
    public void setPrimaryStrength(String primaryStrength) {
    	this.expression.setPrimaryStrength(primaryStrength);
    }
    
    public String getSecondaryStrength() {
    	return this.expression.getSecondaryStrength();
    }
    
    public void setSecondaryStrength(String secondaryStrength) {
    	this.expression.setSecondaryStrength(secondaryStrength);
    }
    
    public ExpressionPattern[] getPatterns() {
    	return this.patterns;
    }
    
    public void setPatterns(ExpressionPattern[] patterns) {
    	this.patterns = patterns;
    }
    
    public boolean getNoPatternCheckedForDeletion() {
    	return this.noPatternCheckedForDeletion;
    }

    public String getNotes() {
    	return this.expression.getExpressionNote();
    }
    
    public void setNotes(String notes) {
    	this.expression.setExpressionNote(notes);
    }
    
    /**
     * @author xingjun - 02/10/2008
     * <p>modified by xingjun - 24/02/2009 - bug fixing:
     *    pattern & location value could be null when doing the comparison</p>
     * @param pattern1
     * @param pattern2
     * @return 1 - patterns are identical
     *         0 - patterns are not identical
     */
    private int comparePatterns(ExpressionPattern[] pattern1, ExpressionPattern[] pattern2) {
//    	System.out.println("now compare two patterns!!!!!");
    	if (pattern1 == null || pattern1[0].getPattern() == null) {
//    		System.out.println("pattern1 is null!!!!!");
    		if (pattern2 == null || pattern2[0].getPattern() == null) {
//    			System.out.println("pattern2 is null!!!!!");
    			return 1;
    		}
    	} else { // pattern1 is not null
//    		System.out.println("pattern1 is not null!!!!!");
    		if (pattern2 != null && pattern2[0].getPattern() != null) {
    			int pLen1 = pattern1.length;
    			int pLen2 = pattern2.length;
    			if (pLen1 == pLen2) {
    				int patternsIdentical = 1;
    				for (int i=0;i<pLen1;i++) {
    					// convert pattern & location value to "null" when they are null
    					String pattern1Value = pattern1[i].getPattern();
//    					System.out.println("p1: " + pattern1Value);
    					pattern1Value = (pattern1Value == null?"null":pattern1Value);
    					String location1Value = pattern1[i].getLocations();
//    					System.out.println("l1: " + location1Value);
    					location1Value = (location1Value == null?"null":location1Value);
						int identical = 0;
    					for (int j=0;j<pLen2;j++) {
        					String pattern2Value = pattern2[j].getPattern();
//        					System.out.println("p2: " + pattern2Value);
        					pattern2Value = (pattern2Value == null?"null":pattern2Value);
        					String location2Value = pattern2[i].getLocations();
//        					System.out.println("l2: " + location2Value);
        					location2Value = (location2Value == null?"null":location2Value);
    						if (pattern2Value.equals(pattern1Value)
    								&& location2Value.equals(location1Value)) {
    							identical = 1;
    							break;
    						}
    					}
						patternsIdentical *= identical;
    				}
    				if (patternsIdentical == 1) {
    					return 1;
    				}
    			}
    		}
//    		System.out.println("pattern2 is null!!!!!");
    	}
    	return 0;
    }
    
    ////////////////////////////////////////////////////////////////////////////////
    /** below methods are copied from EditExpressionBean to make the working process
     * simpler, easier */
    ////////////////////////////////////////////////////////////////////////////////
    
    /**
     * @author xingjun - 03/03/2009
     * @return
     */
    public String addPattern() {
//    	System.out.println("supportBean@addPattern value: ");
    	String primaryStrength = FacesUtil.getRequestParamValue("primaryStrength");
    	
    	// check strength. can only add pattern when the strength value is 'present'/'uncertain'
    	// modified by xingjun - 25/06/2008 - can add pattern if the expression is 'uncertain' as well
    	if (primaryStrength.equals("present") || primaryStrength.equals("uncertain")) {
        	// only execute when there's no patter is being added
            String addingPattern = (String)FacesUtil.getSessionValue("addingPattern");
//            System.out.println("addPattern value: " + addingPattern);
            
            if (addingPattern == null || addingPattern.equals("false")) {
            	if (this.eeBean.getPatterns() == null) { // linked pattern info; need to add one
            		ExpressionPattern[] ep = new ExpressionPattern[1];
            		ep[0] = new ExpressionPattern();
            		this.eeBean.setPatterns(ep);
            	} else { // add one extra pattern record into current pattern array
            		ExpressionPattern[] currentPatterns = this.eeBean.getPatterns();
            		int len = currentPatterns.length;
            		ExpressionPattern[] newPatterns =  new ExpressionPattern[len + 1];
                	for (int i=0;i<len;i++) {
                		newPatterns[i] = currentPatterns[i];
                	}
                	newPatterns[len] = new ExpressionPattern();
                	this.eeBean.setPatterns(newPatterns);
                	this.eeBean.setNoPatternCheckedForDeletion(false);
            	}
            	// set session value: adding pattern
                FacesUtil.setSessionValue("addingPattern", "true");
            } // end of checking session value 'addingPattern'
       }
    	return "editExpression";
    } // end of addPattern
    
    /**
     * @author xingjun - 03/03/2009
     * @return
     */
    public String deletePattern() {
//    	System.out.println("supportBean@deletePattern: ");
    	String primaryStrength = FacesUtil.getRequestParamValue("primaryStrength");

    	// check strength. can only delete pattern when the strength value is 'present'
    	if (primaryStrength.equals("present") || primaryStrength.equals("uncertain")) {
    		this.eeBean.setNoPatternCheckedForDeletion(false);
    		
    		if (this.eeBean.getPatterns() == null) { // cannot delete if there's no pattern info
        		this.eeBean.setNoPatternCheckedForDeletion(false);
    		} else {
    			// check how many patterns have been checked;
    			ExpressionPattern[] currentPatterns = this.eeBean.getPatterns();
    			int numberOfCheckedPattern = 
    				this.getNumberOfPatternsMarkedForDeletion(currentPatterns);
    			if (numberOfCheckedPattern < 1) {
            		this.eeBean.setNoPatternCheckedForDeletion(true);
    			} else { // restructure the patterns
            		int len = currentPatterns.length;
            		if (len == numberOfCheckedPattern) { // delete all patterns
            			this.eeBean.setPatterns(null);
                		this.eeBean.setNoPatternCheckedForDeletion(false);
            			// set session value: adding pattern
                        FacesUtil.setSessionValue("addingPattern", "false");
            		} else { // delete part of patterns
            			int newLen = len - numberOfCheckedPattern;
            			ExpressionPattern[] newPatterns = new ExpressionPattern[newLen];
            			for (int i=0,j=0;i<len;i++) {
            				if (!currentPatterns[i].isSelected()) {
            					newPatterns[j] = currentPatterns[i];
            					j++;
            				}
            			}
            			this.eeBean.setPatterns(newPatterns);
                		this.eeBean.setNoPatternCheckedForDeletion(false);
            			
            			// if added pattern has been checked, delete it and allow user to add new pattern
            			String patternValue = currentPatterns[newLen - 1].getPattern();
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
//    	System.out.println("supportBean@cancelModification!!!!!!");
        // get expression detail
//    	String submissionId = FacesUtil.getRequestParamValue("submissionId");
//        String componentId = FacesUtil.getRequestParamValue("componentId");
        String submissionId = this.eeBean.getSubmissionId();
        String componentId = this.eeBean.getComponentId();
        ExpressionDetail expressionDetail = 
        	editExpressionAssembler.getExpression(submissionId, componentId);
    	this.eeBean.setExpressionDetail(expressionDetail);

        // get pattern infomation
        if (expressionDetail.isAnnotated()) {
        	ExpressionPattern[] pattern = 
        		editExpressionAssembler.getPatterns(String.valueOf(expressionDetail.getExpressionId())); 
          	this.eeBean.setPatterns(pattern);
        } else {
        	this.eeBean.setPatterns(null);
        }
        // reset
        FacesUtil.setSessionValue("addingPattern", "false");
		this.eeBean.setNoPatternCheckedForDeletion(false);
		this.eeBean.setErrorCode(0);
    	return "editExpression";
    }
    
    /**
     * @author xingjun - 03/03/2009
     * <p>if user change strength from 'present'/'uncertain' to other, 
     * the pattern value should be set to blank </p>
     * @param value
     * @return
     */
    public String changeStrength(ValueChangeEvent value) {
//    	System.out.println("supportBean@changeStrength!!!!!!");
    	String strengthValue = (String)value.getNewValue();
    	if (!strengthValue.equals("present") && !strengthValue.equals("uncertain")) {
        	this.eeBean.setPatterns(null);
            FacesUtil.setSessionValue("addingPattern", "false");
    		this.eeBean.setNoPatternCheckedForDeletion(false);
    	}
    	return null;
    }
    
    /**
     * @author xingjun - 02/03/2009
     * @param value
     * @return
     */
    public String changePattern(ValueChangeEvent value) {
//    	System.out.println("supportBean@changePattern!!!!!!");
    	String patternValue = (String)value.getNewValue();
    	if (patternValue.equals("") ) {
    		// when user change pattern into "",
    		// location value should be set to ""
    	}
    	return null;
    }
    
    /**
     * @author xingjun - 03/03/2009
     * <p>if user change location from 'adjacent to xxxxx', 
     * the numerical part of it should be set to blank</p>
     * @param value
     * @return
     */
    public String changeLocation(ValueChangeEvent value) {
//    	System.out.println("supportBean@changeLocation!!!!!!");
    	String locationValue = (String)value.getNewValue();
    	if (!locationValue.equals("adjacent to")) {
    		// do nothing, wait for application refresh the page
    	}
    	return null;
    }
    
    /**
     * @author xingjun - 03/03/2009
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
//    	System.out.println("supportBean@updateExpression!!!!!!");
    	// cannot edit if not logged in
    	String userName = this.eeBean.getUserName();
    	if (userName == null) return null;
    	
    	// get expression detail from database
//    	String submissionId = FacesUtil.getRequestParamValue("submissionId");
//        String componentId = FacesUtil.getRequestParamValue("componentId");
        String submissionId = this.eeBean.getSubmissionId();
        String componentId = this.eeBean.getComponentId();
//        System.out.println("supportBean@updateExpression@submissionId: " + submissionId);
//        System.out.println("supportBean@updateExpression@componentId: " + componentId);

        //        System.out.println("VIEW ID:"+FacesContext.getCurrentInstance().getViewRoot().getViewId());
        // modified by xingjun - 04/08/2008 - pertain to locking function
//      modified by ying 20080909
        if (!FacesContext.getCurrentInstance().getViewRoot().getViewId().equalsIgnoreCase("/pages/ish_edit_pattern.jsp") 
        		&& !this.eeBean.lockSubmission(submissionId)) {
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
//        	System.out.println("ExpressionId: " + this.eeBean.getExpressionDetail().getExpressionId());
//        	System.out.println("ExpressionIdInDB: " + expressionInfoInDB.getExpressionId());
        	patternInfoInDB = editExpressionAssembler.getPatterns(String.valueOf(expressionInfoInDB.getExpressionId()));
//        	if (patternInfoInDB == null) System.out.println("patternInfoInDB is null#######");
        } else {
//        	System.out.println("not annotated#######");
        	patternInfoInDB = null;
        }
        String notesInDB = expressionInfoInDB.getExpressionNote();
        String notesOnPage = this.eeBean.getExpressionDetail().getExpressionNote();
        
        /** strength and pattern */
        if (!expressionInfoInDB.isAnnotated()) { // not annotated in database
        	if (this.eeBean.getExpressionDetail().getPrimaryStrength() != null
        			&& !this.eeBean.getExpressionDetail().getPrimaryStrength().equals("")) { // annotated on page
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
    			int errorCode =
    				editExpressionAssembler.addAnnotation(submissionId, componentId, 
    						this.eeBean.getExpressionDetail().getStage(),
    						this.eeBean.getExpressionDetail().getPrimaryStrength(),
    						this.eeBean.getExpressionDetail().getSecondaryStrength(), 
    						this.eeBean.getPatterns(), userName);
    			this.eeBean.setErrorCode(errorCode);
    			if (errorCode != 0) { // updating failed, reset the strength value
//    				this.eeBean.getExpressionDetail().setPrimaryStrength(expressionInfoInDB.getPrimaryStrength());
    				return "editExpression";
    			}
        	}
        } else { // annotated in database
        	if (this.eeBean.getExpressionDetail().getPrimaryStrength() != null &&
        			!this.eeBean.getExpressionDetail().getPrimaryStrength().equals("")) { // expressed on page
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
        		ExpressionPattern[] patternInfoOnPage = this.eeBean.getPatterns();
//        		if (patterns == null || patterns.length == 0 
//        				|| patterns[0] == null || patterns[0].getPattern() == null 
//        				|| patterns[0].getPattern().equals("")) {
//        			System.out.println("editExpressionBean@patternOnPage is null");
//        		}
//        		if (patternInfoInDB == null || patternInfoInDB.length == 0 
//        				|| patternInfoInDB[0] == null || patternInfoInDB[0].getPattern() == null 
//        				|| patternInfoInDB[0].getPattern().equals("")) {
//        			System.out.println("editExpressionBean@patternInDB is null");
//        		}
        		int errorCode =
        			editExpressionAssembler.updateAnnotation(submissionId, componentId, 
        					this.eeBean.getExpressionDetail().getStage(),
        					this.eeBean.getExpressionDetail().getPrimaryStrength(), 
        					expressionInfoInDB.getPrimaryStrength(),
        					this.eeBean.getExpressionDetail().getSecondaryStrength(), 
        					expressionInfoInDB.getSecondaryStrength(),
        					patternInfoOnPage, patternInfoInDB, userName);
    			this.eeBean.setErrorCode(errorCode);
        		if (errorCode != 0) {
        			return "editExpression";
        		}
        	} else { // not expressed on page
        		// delete notes in database if there are any
        		// log it
        		// modified by xingjun - 28/05/2008 - it's possible there's empty string note exists
        		if (notesInDB != null && notesInDB.trim().length() > 0) {
            		boolean expressionUpdated =
            			editExpressionAssembler.deleteNote(submissionId, componentId, userName);
            		this.eeBean.setExpressionUpdated(expressionUpdated);
            		///// may need to evaluate the success of deleting notes //////
            		if (expressionUpdated) { // after deleting, shouldn't have strength value
            			this.eeBean.getExpressionDetail().setExpressionNote(null);
            		} else {
            			return "editExpression";
            		}
        		}

        		// update primary strength in database to specified value
        		// update secondary strength in database to ""
        		// delete pattern and/or location from database
        		// log it
        		boolean expressionUpdated =
        			editExpressionAssembler.deleteAnnotation(submissionId, componentId, 
        					patternInfoInDB, userName);
        		this.eeBean.setExpressionUpdated(expressionUpdated);
        		if (expressionUpdated) { // after deleting, shouldn't have strength value
        			this.eeBean.getExpressionDetail().setPrimaryStrength(null);
        			this.eeBean.getExpressionDetail().setSecondaryStrength(null);
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
        		boolean expressionUpdated =
        			editExpressionAssembler.addNote(submissionId, componentId, notesOnPage, userName);
        		this.eeBean.setExpressionUpdated(expressionUpdated);
        		if (!expressionUpdated) {
        			return "editExpression";
        		}
        	}
        } else { // notesInDB is not null
        	if (notesOnPage == null || notesOnPage.trim().length() < 1) { // modified by Xingjun - 28/05/2008 - when user delete notes by deleting all characters in the note textfield
        		// delete notes in database;
        		// log it
        		boolean expressionUpdated =
        			editExpressionAssembler.deleteNote(submissionId, componentId, userName);
        		this.eeBean.setExpressionUpdated(expressionUpdated);
        		if (!expressionUpdated) {
        			return "editExpression";
        		}
        	} else {
        		if (!notesInDB.equals(notesOnPage)) {
        			// update notes in database
        			// log it
            		boolean expressionUpdated =
            			editExpressionAssembler.updateNote(submissionId, componentId, notesOnPage, userName);
            		this.eeBean.setExpressionUpdated(expressionUpdated);
            		if (!expressionUpdated) {
            			return "editExpression";
            		}
        		}
        	}
        }
        
        // reset
        FacesUtil.setSessionValue("addingPattern", "false");
        this.eeBean.setNoPatternCheckedForDeletion(false);
        this.eeBean.setErrorCode(0);
        return "editExpression";
    } // end of updateExpression()
    
    
    /**
     * @author xingjun - 24/06/2008
     * <p>modified by xingjun - moved here from EditExpressionBean and modified as appropriate</p>
     * @return
     */
    public String updateExpressionNote() {
//    	System.out.println("supportBean@updateExpressionNote!!!!!!");
    	// cannot edit if not logged in
    	String userName = this.eeBean.getUserName();
    	if (userName == null) return null;
    	
    	// get expression detail from database
//    	String submissionId = FacesUtil.getRequestParamValue("submissionId");
//        String componentId = FacesUtil.getRequestParamValue("componentId");
        String submissionId = this.eeBean.getSubmissionId();
        String componentId = this.eeBean.getComponentId();
//        System.out.println("supportBean@updateExpressionNote@submissionId: " + submissionId);
//        System.out.println("supportBean@updateExpressionNote@componentId: " + componentId);

        // modified by xingjun - 04/08/2008 - pertain to locking function
        if (!this.eeBean.lockSubmission(submissionId)) {
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
        String notesOnPage = this.eeBean.getExpressionDetail().getExpressionNote();
        
        /** expression note */
        // executed when not deleting annotation
        if (notesInDB == null ) {
        	if (notesOnPage != null && notesOnPage.trim().length() > 0) {
        		// insert notes into database
        		// log it
        		boolean expressionUpdated =
        			editExpressionAssembler.addNote(submissionId, componentId, notesOnPage, userName);
        		this.eeBean.setExpressionUpdated(expressionUpdated);
        		if (!expressionUpdated) {
        			return "editExpression";
        		}
        	}
        } else { // notesInDB is not null
        	// modified by Xingjun - 28/05/2008 
        	//- when user delete notes by deleting all characters in the note textfield
        	if (notesOnPage == null || notesOnPage.trim().length() < 1) {
        		// delete notes in database;
        		// log it
        		boolean expressionUpdated =
        			editExpressionAssembler.deleteNote(submissionId, componentId, userName);
        		this.eeBean.setExpressionUpdated(expressionUpdated);
        		if (!expressionUpdated) {
        			return "editExpression";
        		}
        	} else {
        		if (!notesInDB.equals(notesOnPage)) {
        			// update notes in database
        			// log it
            		boolean expressionUpdated =
            			editExpressionAssembler.updateNote(submissionId, componentId, notesOnPage, userName);
            		this.eeBean.setExpressionUpdated(expressionUpdated);
            		if (!expressionUpdated) {
            			return "editExpression";
            		}
        		}
        	}
        }
        
        // reset parameters
        FacesUtil.setSessionValue("addingPattern", "false");
        this.eeBean.setNoPatternCheckedForDeletion(false);
        this.eeBean.setErrorCode(0);
        return "editExpression";
    }
    
    /**
     * @author xingjun - 03/03/2009
	 * <p>modified by xingjun - 28/09/2009
	 * - added code to initialise the eeBean if eeBean is not initialised</p>
     * @return
     */
    public String unlockSubmission() {
//    	System.out.println("eeSupportBean@unlockSubmission!!!!!!");
    	String lockingSwitchOn = Utility.getLockingSwitchOn();
    	if (lockingSwitchOn.equals("false")) { // locking function switched off
    		return "editExpression";
    	}
		if (this.eeBean == null) {
			this.eeBean = new EditExpressionBean();
			this.eeBean.setUser(this.user);
		}
    	String submissionId = FacesUtil.getRequestParamValue("submissionId");
//    	String submissionId = this.expression.getSubmissionId();
//    	System.out.println("eeSupportBean:unlockSubmission:submissionId: " + submissionId);
		int privilege = Utility.getUserPriviledges();
		if (privilege < 5) {
			this.eeBean.setErrorCode(5);
			return "editExpression";
		}
		int submissionUnlocked = EditAssemblerUtil.unlockSubmission(submissionId);
		if (submissionUnlocked == 0) {
			this.eeBean.setErrorCode(7);
		} else {
			this.eeBean.setErrorCode(0);
			this.eeBean.setLockingText("UNLOCKED");
			this.eeBean.setLockingTextDisplayStyle(Utility.getUnlockedTextDisplayStyle());
			this.eeBean.setUnlocking(true);
		}
//		System.out.println("eeSupportBean:errorCode: " + errorCode);
    	return "editExpression";
    }
    
    /**
     * @author ying
     * modified by xingjun - 03/03/2009 
     * - moved here from EditExpressionBean and modified as appropriate
     * @return
     */
    public String annotationSignOff() {
//    	System.out.println("EditExpressionSupportBean@annotationSignOff!!!!!!");
    	String submissionId = null;
    	if(null != getRequestSubmissionId()) {
    		submissionId = getRequestSubmissionId();
    	} else {
    		submissionId = this.eeBean.getSubmissionId();
    	}
//    	System.out.println("signoff:"+submissionId);

    	// modified by xingjun - 04/08/2008 - lock submission before sign off the submission
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
			this.eeBean.setErrorCode(5);
			return "LabIshEdit";
		}
		int submissionUnlocked =
			EditAssemblerUtil.unlockSubmission(submissionId);
		if (submissionUnlocked == 0) {
			this.eeBean.setErrorCode(7);
		} else {
			this.eeBean.setErrorCode(0);
			this.eeBean.setLockingText("UNLOCKED");
			this.eeBean.setLockingTextDisplayStyle(Utility.getUnlockedTextDisplayStyle());
			this.eeBean.setUnlocking(true);
		}        
		return "LabIshEdit";
    } // end of annotationSignOff
    
    /**
     * @author Ying
     * <p> xingjun - 03/03/2009 
     * - moved here from EditExpressionBean and modified as appropriate</p>
     * @return
     */
    public String annotationReturnToAnnotator() {
//    	System.out.println("supportBean@annotationReturnToAnnotator!!!!!!");
    	String submissionId = null;
    	if(null != getRequestSubmissionId()) {
    		submissionId = getRequestSubmissionId();
    	} else {
    		submissionId = this.eeBean.getSubmissionId();
    	}
//    	System.out.println("returntoannot:"+submissionId);
    	
        // modified by xingjun - 04/08/2008 - lock the submission before signing off
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
			this.eeBean.setErrorCode(5);
			return "LabIshEdit";
		}
		int submissionUnlocked = EditAssemblerUtil.unlockSubmission(submissionId);
		if (submissionUnlocked == 0) {
			this.eeBean.setErrorCode(7);
		} else {
			this.eeBean.setErrorCode(0);
			this.eeBean.setLockingText("UNLOCKED");
			this.eeBean.setLockingTextDisplayStyle(Utility.getUnlockedTextDisplayStyle());
			this.eeBean.setUnlocking(true);
		}         
        return "LabIshEdit";
    }    
    
    /**
     * @author ying
     * modified by xingjun - 03/03/2009 
     * - moved here from EditExpressionBean and modified as appropriate
     * @return
     */
    public String annotationReturnToEditor() {
//    	System.out.println("supportBean@annotationReturnToEditor!!!!!!");
    	String submissionId = null;
    	if(null != getRequestSubmissionId()) {
    		submissionId = getRequestSubmissionId();
    	} else {
    		submissionId = this.eeBean.getSubmissionId();
    	}
//    	System.out.println("returntoEditor:"+submissionId);
    	
        // modified by xingjun - 04/08/2008 - locking function switched on
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
			this.eeBean.setErrorCode(5);
			return "LabIshEdit";
		}
		int submissionUnlocked = EditAssemblerUtil.unlockSubmission(submissionId);
		if (submissionUnlocked == 0) {
			this.eeBean.setErrorCode(7);
		} else {
			this.eeBean.setErrorCode(0);
			this.eeBean.setLockingText("UNLOCKED");
			this.eeBean.setLockingTextDisplayStyle(Utility.getUnlockedTextDisplayStyle());
			this.eeBean.setUnlocking(true);
		} 
        
        // set sub_is_public=1
        //editExpressionAssembler.setPublicSubmission(submissionId, "0");
        return "LabIshEdit";
    }
    
	/**
	 * @author ying
	 * <p>moved here by xingjun from EditExpressionBean - 03/03/2009</p>
	 * @return
	 */
    private String getRequestSubmissionId() {
		ISHSingleSubmissionBean ub =
			(ISHSingleSubmissionBean)(FacesUtil.getFacesRequestParamObject("ishSubmissionBean"));
		if (ub != null)
			return ub.getSubmission().getAccID();
		return null;
	}
	
	/**
	 * @author xingjun - 04/08/2008
	 * <p>modified by xingjun - 03/03/2009
	 * - moved here from EditExpressionBean and modified as appropriate</p>
	 * <p>modified by xingjun - 28/09/2009
	 * - added code to initialise the eeBean if eeBean is not initialised</p>
	 * @return
	 */
	public boolean lockSubmission(String submissionId) {
//		System.out.println("supportBean@lockSubmission!!!!!!");
//		System.out.println("lockSubmission@submissionId: " + submissionId);
		if (this.eeBean == null) {
			this.eeBean = new EditExpressionBean();
			this.eeBean.setUser(this.user);
		}
		String lockingSwitchOn = Utility.getLockingSwitchOn();
        if (lockingSwitchOn.equals("true")) { // locking function switch on
            // check locking info - do nothing if locked, lock the submission if unlocked
        	LockingInfo lockingInfo = EditAssemblerUtil.getLockingInfo(submissionId);if (lockingInfo == null) System.out.println("failed to get lockingInfo");
        	if (lockingInfo != null) {
            	this.eeBean.setLockingInfo(lockingInfo);
        		String lb = lockingInfo.getLockedBy();
        		int lockTime = lockingInfo.getLockTime();
        		int definedLockTimeOut = Integer.parseInt(Utility.getLockTimeOut());
        		// might be locked by others
        		if (!this.eeBean.getUserName().equals(lb) 
        				&& (lockTime-definedLockTimeOut < 0 || lockTime-definedLockTimeOut == 0)) { // locked
//        			this.eeBean.setSubmissionLocked(true);
        			this.eeBean.setLockingText("LOCKED by " + lb + ". Cannot modify");
        			this.eeBean.setLockingTextDisplayStyle(Utility.getLockedTextDisplayStyle());
        			return false;
        		} else { // re-lock - update lock time;
//        			int submissionLocked =
//        				EditAssemblerUtil.relockSubmission(submissionId, this.eeBean.getUserName());
        			int submissionLocked =
        				EditAssemblerUtil.relockSubmission(submissionId, this.eeBean.getUser());
        			if (submissionLocked == 0) { // failed to re-lock
        				this.eeBean.setErrorCode(7);
        				return false;
        			}
        		}
        	} else { // unlocked - need to lock
//        		System.out.println("lock submission by eeSupportBean: update expression!");
//    			int submissionLocked =
//    				EditAssemblerUtil.lockSubmission(submissionId, this.eeBean.getUserName());
    			int submissionLocked =
    				EditAssemblerUtil.lockSubmission(submissionId, this.eeBean.getUser());
    			if (submissionLocked == 0) { // failed to lock
    				this.eeBean.setErrorCode(7);
    				return false;
    			}
        	}
			this.eeBean.setErrorCode(0);
//    		this.eeBean.setSubmissionLocked(false);
			this.eeBean.setLockingText("LOCKED by " + this.eeBean.getUserName());
			this.eeBean.setLockingTextDisplayStyle(Utility.getLockedTextDisplayStyle());
        }
        // successful
        return true;
	}

}
