package gmerg.beans;

import gmerg.assemblers.ISHSubmissionAssembler;
import gmerg.entities.submission.Antibody;
import gmerg.entities.submission.ish.ISHSubmission;
import gmerg.utils.CookieOperations;
import gmerg.utils.FacesUtil;

import javax.faces.context.FacesContext;

public class ISHSingleSubmissionBean {

    private ISHSubmission submission;
    private ISHSubmissionAssembler assembler;
    protected String submissionID;
    protected String id;
    private String annotationDisplayType;
    private String displayOfAnnoGps;
    private String originalURL = "/pages/ish_submission.html";
    private boolean renderPage;
    private boolean renderPrbSeqInfo;
    private boolean renderPrbNameURL;
    private boolean expressionMapped;
    
    public ISHSingleSubmissionBean() {
		assembler = new ISHSubmissionAssembler();
		id = FacesUtil.getRequestParamValue("id");
		if (id == null || id.equals("")) 
			id = (String) FacesUtil.getSessionValue("id");
		annotationDisplayType = null;
		displayOfAnnoGps = null;
		boolean displayCommand = false;
		if (id != null && !id.equals("")) {
			FacesUtil.setSessionValue("id", id);
			// get the type of annotation display (tree or list)
			annotationDisplayType = FacesUtil.getRequestParamValue("annotationDisplay");
			if (annotationDisplayType == null)
				annotationDisplayType = CookieOperations.getCookieValue("annotationDisplay");
			else
				displayCommand = true;

			displayOfAnnoGps = FacesUtil.getRequestParamValue("displayOfAnnoGps");
			if (displayOfAnnoGps == null || displayOfAnnoGps.equals("")) {
				displayOfAnnoGps = CookieOperations.getCookieValue("displayOfAnnoGps");
				if (displayOfAnnoGps == null || displayOfAnnoGps.equals("")) {
					displayOfAnnoGps = "show";
					CookieOperations.setValueInCookie("displayOfAnnoGps", displayOfAnnoGps, -1, false);
				}
			} else
				displayCommand = true;
			if (!displayCommand) // If it is an display change actionlink then do setSubmission in the action method
				// get a submission using the id provided
				setSubmissionDetails(id);
		}
	}
        
    /**
	 * <p>
	 * method to query send id parameters to the DAO to query the db and set the
	 * details as a submission object. Method also carries out some post db
	 * query processing of the submission
	 * </p>
	 * <p>
	 * modified by xingjun - 27/08/2008 - added code linked to transgenic data
	 * </p>
	 * 
	 * @param subId -
	 *            the accession id of the submission
	 */
    public void setSubmissionDetails(String subId){
       
		boolean displayAsTree = false;
        if(annotationDisplayType == null || annotationDisplayType.equals("tree") || annotationDisplayType.equals(""))
            displayAsTree = true;
        
        // find if the editor is logged in
        Object object = FacesUtil.getSessionValue("userBean");

        boolean isEditor = false;
        UserBean ub = null;
        boolean onlyRetrieveTree = false;
        if (object != null) {
        	ub = (UserBean)object;
        	isEditor = ub.isUserLoggedIn();
        	
        	//System.out.println("GDGDGDG:"+FacesContext.getCurrentInstance().getViewRoot().getViewId());
        	if(null != ub.getUser() && ub.getUser().getUserPrivilege()>=3 && FacesContext.getCurrentInstance().getViewRoot().getViewId().equalsIgnoreCase("/pages/ish_edit_expression.jsp")) {
        		displayAsTree = true;
        		onlyRetrieveTree = true;
        	}
        }
        //set the instance variable 'submission' from the value obtained from the DAO
        submission = assembler.getData(subId, isEditor, displayAsTree, ub, onlyRetrieveTree);
        
        //if submission is not null, perform post-processing
        if (submission != null){
            // initialise probe/antibody sequence info
            // modify to allow to display IHC data -- xingjun 09-July-2007
            String assayType = submission.getAssayType();
            this.setSubmissionID(subId);
            if(!onlyRetrieveTree) {
	            if (assayType.indexOf("ISH") >=0) { // ISH data
	//            	System.out.println("assayType: ISH");
	            	if (submission.getProbe().getSeqStatus() != null && !submission.getProbe().getSeqStatus().equals("Unsequenced.")) {
	            		renderPrbSeqInfo = true;
	                    if (submission.getProbe().getSeq5Loc().equals("n/a") ||
	                        submission.getProbe().getSeq3Loc().equals("n/a")) {
	                        submission.getProbe().setSeqInfo("Accession number for part sequence: ");
	                    } else {
	                        submission.getProbe().setSeqInfo("Probe sequence spans from " +
	                                                         submission.getProbe().getSeq5Loc() +
	                                                         " to " +
	                                                         submission.getProbe().getSeq3Loc() +
	                                                         " of");
	                    }
	                    if (submission.getProbe().getProbeNameURL()!= null && !(submission.getProbe().getProbeName().indexOf("maprobe") >=0)){
	                        renderPrbNameURL = true;
	                    }   
	                }
	            } else if (assayType.indexOf("IHC") >= 0) { // IHC data
//	            	System.out.println("assayType: IHC");
	            	Antibody antibody = submission.getAntibody();
	            	if (antibody.getSeqStatus() != null && !antibody.getSeqStatus().equals("Unsequenced.")) {
	            		renderPrbSeqInfo = true;
	            		if (antibody.getSeqStartLocation() == 0 || antibody.getSeqEndLocation() == 0) {
	            			antibody.setSeqInfo("Accession number for part sequence: ");
	            		}else {
	            			antibody.setSeqInfo("Specific sequence recognized: aa " + antibody.getSeqStartLocation() +
	            					" - " + antibody.getSeqEndLocation() + " of ");
	            		}
	            		if (antibody.getUrl() != null && antibody.getUrl() != "") {
	            			renderPrbNameURL = true;
	            		}
	            	}
	            } else if (assayType.indexOf("TG") >= 0) { // transgenic data
	            	// System.out.println("assayType: transgenic");
	            } else { // other assay type
	            	
	            }
            }
            
            if (submission.getAnnotationTree() != null || submission.getAnnotatedComponents() != null){
                expressionMapped = true;
            }
        }
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {   
        this.id = id;
    }
    
    public void setSubmission(ISHSubmission sub) {
        submission = sub;
    }

    public ISHSubmission getSubmission() {
        return submission;
    }
    
    public String getTransgenicTitle() {
    	String type = submission.getTransgenics()[0].getMutantType();
    	if ("transgenic insertion".equalsIgnoreCase(type))
    		return "Transgenic Reporter Allele"+ ((submission.isMultipleTransgenics())?"s":"");
    	if ("mutant allele".equalsIgnoreCase(type))
    		return "Mutant Allele"+ ((submission.isMultipleTransgenics())?"s":"");
    	return "";
    }
    
    public boolean isRenderPage() {
    
        if(submission == null){
            renderPage = false;
        }
        else {
            renderPage = true;
        }
        return renderPage;
    }
    
    public boolean isRenderPrbSeqInfo(){
        return renderPrbSeqInfo;
    }
    
    public boolean isRenderPrbNameURL(){
        return renderPrbNameURL;
    }
    
    public boolean isExpressionMapped() {
        return expressionMapped;
    }
    
    public String getAnnotationDisplayLinkTxt() {
        if (annotationDisplayType == null || annotationDisplayType.equals("tree") || annotationDisplayType.equals(""))
            return "View annotated components as a list";
        else
            return "View annotated components as a tree";
    }
    
    /**
     * modified by xingjun - 06/02/2009 
     * - changed the default value of displayOfAnnoGps to 'show'
     * @return
     */
    public String getDisplayOfAnnotatedGpsTxt() {
        if (displayOfAnnoGps == null || displayOfAnnoGps.equals("hide"))
        	return "Show annotation under groups";
        else
        	return "Hide annotation under groups";
    }
    
    public String getSubmissionID() {
        return submissionID;
    }
    
    public void setOriginalURL(String value){
        originalURL = value;
    }
    /**
     * used by faces navigation to redirect from one URL to another
     * @return a string containing a URL
     */
    public String getOriginalURL() {
        return originalURL+"?id="+submissionID;
    }

    public void setSubmissionID(String submissionID) {
        this.submissionID = submissionID;
    }
    
    
	/**
	 * method to tell toggle between displaying annotation as a list or a tree
	 * @return string literal telling faces navigation that the operation was a success
	 */
	public String annotation() {
		if(annotationDisplayType == null || annotationDisplayType.equals("tree") || annotationDisplayType.equals(""))
			annotationDisplayType = "list";
		else 
			annotationDisplayType = "tree";
		setSubmissionDetails(id);
		CookieOperations.setValueInCookie("annotationDisplay", annotationDisplayType, -1, false);

		return "success";
    }
    
    /**
     * modified by xingjun - 06/02/2009 - changed the default value of displayOfAnnoGps to 'show'
     * method to tell the application whether to display annotated components under 
     * group terms or not
     * @return string literal to tell faces navigation that the operation was a success
     */
    public String displayOfAnnotatedGps() {
		if (displayOfAnnoGps==null || displayOfAnnoGps.equals("show")) 
			displayOfAnnoGps = "hide";
		else 
			displayOfAnnoGps = "show";
		setSubmissionDetails(id);
        CookieOperations.setValueInCookie("displayOfAnnoGps", displayOfAnnoGps, -1, false);

        return "success";
    }

	public String getAnnotationDisplayType() {
		return annotationDisplayType;
	}

	public String getDisplayOfAnnoGps() {
		return displayOfAnnoGps;
	}
    
}
