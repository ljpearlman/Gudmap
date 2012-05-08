package gmerg.beans;

import gmerg.assemblers.AnatomyStructureAssembler;

import gmerg.utils.FacesUtil;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

/**
 * The AnatomyBean makes anatomy-based query requests to the DAO and controls display of results of queries to users
 * @author
 */
public class AnatomyBean {

    private SelectItem[] displayStageRange;
    
    private AnatomyStructureAssembler assembler;
    
    private ResourceBundle bundle;
    
    private String startStage; //value of user-defined startStage
    private String endStage;  //value of user-defined end stage
    private ArrayList treeContent;
    //private String expressionStatus; //determines whether user want to find genes present or absent
    private String criteria; //determines whether to find assays with expression annotation in 'all' or 'any' of selected components.
    private String [] expressionTypes;
    private boolean stagesSelected;
    
    private UIInput startInput;
    private UIInput endInput;
    
    private UISelectMany expressionInput;
    private UISelectOne criteriaInput;
    
    private String resultURL;

    public AnatomyBean() {

        
        assembler = new AnatomyStructureAssembler();
        ArrayList stageRange = assembler.getStageRanges();
        displayStageRange = new SelectItem [stageRange.size()];
        
        for(int i=0;i<displayStageRange.length;i++){
            String [] stage = (String []) stageRange.get(i);
            displayStageRange[i] = new SelectItem(stage[0]);
        }
        
        startStage = (String) displayStageRange[0].getValue();
        endStage = (String) displayStageRange[displayStageRange.length-1].getValue();

        treeContent = new ArrayList();
        treeContent = assembler.buildTree(startStage, endStage, false);
        
        bundle = ResourceBundle.getBundle("configuration");

    }
    
    /**
     * method to get all the terms within the anatomy ontology
     * @return a string literal containing the list of terms
     */
    public String getOntologyTerms() {
    	return assembler.getOntologyTerms();
    }
    
    public UIInput getStartInput() { return startInput; }
    public void setStartInput(UIInput value) { startInput = value; }
    
    public UIInput getEndInput() { return endInput; }
    public void setEndInput(UIInput value) { endInput = value; }
    
    public UISelectMany getExpressionInput() { return expressionInput; }
    public void setExpressionInput(UISelectMany value){ expressionInput = value; }
    
    public UISelectOne getCriteriaInput() { return criteriaInput; }
    public void setCriteriaInput(UISelectOne value){ criteriaInput = value; }
    
    //public void setExpressionStatus(String value){
    //    expressionStatus = value;
    //}
    
    //public String getExpressionStatus(){
    //    return expressionStatus;
    //}
    
    public void setCriteria(String value) {
        criteria = value;
    }
    
    public String getCriteria() {
        return criteria;
    }

    public SelectItem[] getAvailableStages() {
        return displayStageRange;
    }
    
    public void setStartStage(String stg){
        startStage = stg;
    }
    
    public String getStartStage(){
        return startStage;
    }
    
    public void setEndStage(String stg){
        endStage = stg;
    }
    
    public String getEndStage(){
        return endStage;
    }
    
    public void setExpressionTypes(String [] value){
        expressionTypes = value;
    }
    
    public String [] getExpressionTypes() {
        return expressionTypes;
    }
    
    public boolean getStagesSelected() {
        if(startStage == null || endStage == null){
            stagesSelected = false;
        }
        else {
            stagesSelected = true;
        }
        return stagesSelected;
    }
    
    public ArrayList getTreeContent(){
        return treeContent;
    }
    
    /**
     * method to make sure user doesn't try to select a starting stage range occurs later than an ending stage range and vice versa
     * @param context - not used
     * @param component - not used
     * @param value - not used
     */
    public void validateStages(FacesContext context, UIComponent component, Object value) {
        
        String s = (String) startInput.getLocalValue();
        String e = (String) endInput.getLocalValue();
        
        boolean stageRangeValid = assembler.stageRangesAreValid(s,e);
        
        if(!stageRangeValid) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a valid stage range. ", "Please select a valid stage range. ");
            throw new ValidatorException(message);
        }
        
        
    }
    
    
    public void validateSearchCriteria(FacesContext context, UIComponent component, Object value) {
        String c = (String) criteriaInput.getLocalValue();
        String [] e = (String []) expressionInput.getLocalValue();
        
        if(c == null || c.equals("")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select whether you want to search for submissions with expression in 'any' or 'all' selected components ", "Please select whether you want to search for submissions with expression in 'any' or 'all' selected components ");
            throw new ValidatorException(message);
        }
        
        if(e == null || e.length < 1) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select the type of expression you want to search for in selected components ", "Please select the type of expression you want to search for in selected components ");
            throw new ValidatorException(message);
        }
    }
    /**
     * sets the value of the content used to build an anatomy tree
     * @param e - not used
     */
    public void displayTree(ActionEvent e) {
        treeContent = assembler.buildTree(startStage, endStage, false);
    }
    
    /**
     * navigation method. Create a URL which will find db entries based on users input when making anatomy based searches.
     * The URL will be used by faces navigation object and appropriate redirect will be implemented
     * @return a URL to redirect to
     */
    public String annotatedSubmissions(){
    
        //get the ids of the anatomical components selected by the user 
        String [] componentList = FacesUtil.getRequestParamValues("component");
        String components = "components=";
        for(int i=0;i<componentList.length;i++){
            if(i == componentList.length-1){
                components += componentList[i];
            }
            else {
                components+=componentList[i]+"&components=";
            }
        }
        
        String expressionType = "";
        
        for(int i=0;i<this.getExpressionTypes().length;i++){
            if(i==this.getExpressionTypes().length-1){
                expressionType += this.getExpressionTypes()[i];
            }
            else {
                expressionType += this.getExpressionTypes()[i]+"_";
            }
        }
        
        if (components == null || components.equals("")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please make a valid selection from the tree before querying the database. ", "Please make a valid selection from the tree before querying the database. ");
            throw new ValidatorException(message);
        }
                
        String resultURLQString = "?queryType=genesInAnatomyISH&"+components+"&startStage="+this.getStartStage()+"&endStage="+this.getEndStage()+"&expressionTypes="+expressionType+"&criteria="+this.getCriteria();
        resultURL = "/pages/ish_submissions.html" + resultURLQString;
                
        return "success";
    }
    
    /**
     * XGEbase functionality - when user uses diagram of pronephros to browse data, this navigation function is called.
     * Creates a URL for redirection
     * @return a URL to redirect to.
     */
    public String annotSubsFromPronephBrowse() {
        //get the ids of the anatomical components selected by the user 
        String [] componentList = FacesUtil.getRequestParamValues("component");
        String components = "";
        for(int i=0;i<componentList.length;i++){
            if(i == componentList.length-1){
                components += componentList[i];
            }
            else {
                components+=componentList[i]+"_";
            }
        }
        
        String expressionType = "";
        
        for(int i=0;i<this.getExpressionTypes().length;i++){
            if(i==this.getExpressionTypes().length-1){
                expressionType += this.getExpressionTypes()[i];
            }
            else {
                expressionType += this.getExpressionTypes()[i]+"_";
            }
        }
        
        if (components == null || components.equals("")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please make a valid selection from the tree before querying the database. ", "Please make a valid selection from the tree before querying the database. ");
            throw new ValidatorException(message);
        }
                
        String resultURLQString = "?queryType=genesInAnatomyISH&components="+components+"&startStage="+this.getStartStage()+"&endStage="+this.getEndStage()+"&expressionTypes="+expressionType+"&criteria="+this.getCriteria();
        resultURL = "/pages/ish_submissions.html" + resultURLQString;
                
        return "success";
    }
        
    public String getResultURL () {
        return resultURL;
    }

    public String anatomyQueryPage() {
        return "anatomyTree";
    }
    /**
     * returns a list of stages for which data is available for user to narrow their search on
     * @return SelectItem [] - list of queryable stages.
     */
    public SelectItem[] getAvailableStagesForQuery() {
       
        String species = bundle.getString("species");
        
        if(species!= null && species.equals("Xenopus laevis")) {
            return new SelectItem[] { 
                new SelectItem("", "ALL"), new SelectItem("20","NF20"), new SelectItem("24","NF24"), 
                new SelectItem("25", "NF25"), new SelectItem("27", "NF27"),
                new SelectItem("28","NF28"), new SelectItem("29-30", "NF29-30"),
                new SelectItem("31","NF31"), new SelectItem("32", "NF32"), 
                new SelectItem("33-34", "NF33-34"), new SelectItem("35-36", "NF35-36"), 
                new SelectItem("37-38", "NF37-38"), new SelectItem("40", "NF40")
            };
            
        }
        else {
            return new SelectItem[] { 
                new SelectItem("", "ALL"), new SelectItem("17","TS17"), new SelectItem("18","TS18"), 
                new SelectItem("19", "TS19"), new SelectItem("20", "TS20"),
                new SelectItem("21","TS21"), new SelectItem("22", "TS22"),
                new SelectItem("23","TS23"), new SelectItem("24", "TS24"), 
                new SelectItem("25", "TS25"), new SelectItem("26", "TS26"), 
                new SelectItem("27", "TS27"), new SelectItem("28", "TS28")
            };
        }
    }
    /**
     * returns a list of available annotation parameters for users to use when making queries
     * @return list of annotations
     */
    public SelectItem [] getAnnotationVals() {
        String possOrUncertainParam = "";
        
        if(bundle.getString("project").equals("EuReGene")){
            possOrUncertainParam = "possible";
        }
        else if(bundle.getString("project").equals("GUDMAP")){
            possOrUncertainParam = "uncertain";
        }
        
        return new SelectItem []
        { new SelectItem("present"), new SelectItem("not detected"), new SelectItem(possOrUncertainParam) };
    }
}
