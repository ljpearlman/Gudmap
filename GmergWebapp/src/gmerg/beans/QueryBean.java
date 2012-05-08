package gmerg.beans;

import gmerg.assemblers.QueryAssembler;
import gmerg.entities.submission.SubmissionData;
import gmerg.utils.FacesUtil;
import gmerg.utils.Visit_old;

import java.util.Hashtable;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

public class QueryBean {

    private String inputType;
    private String criteria;
    private String geneSymbol;
    private String stage;
    private String output;
    private String componentID;
    private String assayURL;
    private String submissionID;
    private String resultURL;
    private QueryAssembler queryAssembler;


    public QueryBean() {
        
    }
    
    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
    
    public String getComponentID() {
        return componentID;
    }

    public void setComponentID(String componentID) {
        this.componentID = componentID;
    }       
    
    public String findGenes () {
        
        Hashtable<String, String> params = new Hashtable<String, String>();
        
        if (output == null || output.trim().equals("") || geneSymbol == null ||
            geneSymbol.trim().equals("") || inputType == null ||
            inputType.trim().equals("") || stage == null ||
            criteria == null || criteria.trim().equals("")) {
            return "emptyResult";
        }
        
        if(output.equals("anatomy")) {
        
            Visit_old storedVars = (Visit_old)FacesUtil.getSessionValue("compGeneExpStoredVars");
            if(storedVars == null){
                storedVars = new Visit_old();
            }
            
            storedVars.setQueryType("geneQueryWithAnnotation");
            storedVars.setNumPagesForQuery(-1);
            
            params.put("inputType", inputType);
            params.put("criteria", criteria);
            params.put("geneSymbol", geneSymbol);
            params.put("stage", stage);
            params.put("output", output);
            params.put("ignoreExpression", "false");
            storedVars.setParameters(params);
            
            FacesUtil.setSessionValue("compGeneExpStoredVars", storedVars);
            
            return "success_annotated";
        }
        else {
        
            String resultURLQString = "?queryType=geneQueryISH&inputType="+inputType+"&geneSymbol="+geneSymbol+"&stage="+stage+"&criteria="+criteria+"&output="+output+"&ignoreExpression=true";
            resultURL = "/pages/ish_submissions.html" + resultURLQString;
            return "success_all";
        }

    }
    
    public String findComponent() {
        
        
        if(componentID == null || componentID.equals("")){
            return "emptyResult";
        }
        
        ResourceBundle bundle = ResourceBundle.getBundle("configuration");
        
        String anatomyPrefix = bundle.getString("anatomy_id_prefix");

        if(componentID.indexOf(anatomyPrefix) == -1) {
            componentID = anatomyPrefix + componentID;
        }
        
        String resultURLQString = "?queryType=componentSubsISH&componentID="+componentID;
        resultURL = "/pages/ish_submissions.html" + resultURLQString;
        
        return "success";
    }
    
    public String findSubsExpressedInComponent() {
    
        inputType = "symbol";
        criteria = "equals";
        geneSymbol = FacesUtil.getRequestParamValue("geneSymbol");
        stage = FacesUtil.getRequestParamValue("stage");
        componentID = FacesUtil.getRequestParamValue("component");
        output = "gene";
        
        if(componentID == null || componentID.trim().equals("")) {
            return "emptyResult";
        }
        
        String resultURLQString = "?queryType=componentGeneSubsISH&inputType="+inputType+"&geneSymbol="+geneSymbol+"&stage="+stage+"&component="+componentID+"&criteria="+criteria+"&output="+output+"&ignoreExpression=false";
        resultURL = "/pages/ish_submissions.html" + resultURLQString;
        
        return "success";
        
    }
    
    public String getResultURL () {
        return resultURL;
    }
    
    public void setSubmissionID(String value){
        submissionID = value;
    }
    
    public String getSubmissionID() {
        return submissionID;
    }
    
    public void setAssayURL(String value) {
        assayURL = value;
    }
    
    public String getAssayURL() {
        return assayURL;
    }

    /**
     * determine which page to go to after user has submistted request to
     * find particular submission
     * @return string to determine page navigation
     */
    public String submissionInfo() {

        ResourceBundle bundle = ResourceBundle.getBundle("configuration");

        String submissionPrefix = bundle.getString("submission_id_prefix");

        queryAssembler = new QueryAssembler();
        if (null != submissionID &&
            submissionID.indexOf(submissionPrefix) == -1) {
            submissionID = submissionPrefix + submissionID;
        }
	
	
        Object object = 
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getSessionMap()
                            .get("userBean");
                            
        // This only works if myBean2 is request scoped.
        UserBean myBean2 = null;
        if (object != null) {
        	myBean2 = (UserBean) object; 
        }
        
        SubmissionData detail =
            queryAssembler.getSubmissionQueryData(submissionID, myBean2);
	    
        if (null == detail) {
            return "emptyResult";
        } else if (detail.getSubmissionType() == 0) {
            this.setAssayURL("/pages/ish_submission.html?id=" + submissionID);
        } else {
            this.setAssayURL("/pages/mic_submission.html?id=" + submissionID);
        }
        return "submissionDetail";
    }
    
}