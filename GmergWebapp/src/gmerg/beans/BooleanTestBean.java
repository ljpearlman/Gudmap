package gmerg.beans;

import gmerg.assemblers.AnatomyStructureAssembler;

import java.util.ArrayList;

import gmerg.model.BooleanQueryParserDelegate;
import gmerg.utils.FacesUtil;
import gmerg.utils.FileHandler;
import gmerg.utils.Utility;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletResponse;


/**
 * 
 */
public class BooleanTestBean {
    private boolean debug = false;

    private String startStage1;
    private String endStage1;
    private String [] annotationTypes1;
    private String location1;
    private String pattern1;
    private String locations1;
    
    private String startStage2;
    private String endStage2;
    private String [] annotationTypes2;
    private String location2;
    private String pattern2;
    private String locations2;
    
    private String startStage3;
    private String endStage3;
    private String [] annotationTypes3;
    private String location3;
    private String pattern3;
    private String locations3;
    
    private String operator1;
    private String operator2;
    
    private AnatomyStructureAssembler assembler;
    
    private String startStage;
    private String endStage;
    
    private UIInput startInput;
    private UIInput endInput;
    
    private ArrayList treeContent;
    
    
    private SelectItem[] displayStageRange;

    private String input;
    
    
    public BooleanTestBean() {
	    if (debug)
		System.out.println("BooleanTestBean.constructor");

        assembler = new AnatomyStructureAssembler();
        ArrayList stageRange = assembler.getStageRanges();
        displayStageRange = new SelectItem [stageRange.size()];
        
        for(int i=0;i<displayStageRange.length;i++){
            String [] stage = (String []) stageRange.get(i);
            displayStageRange[i] = new SelectItem(stage[0]);
        }
        
        startStage = (String) displayStageRange[0].getValue();
        endStage = (String) displayStageRange[displayStageRange.length-1].getValue();
        endStage1 = endStage2 = endStage3 = endStage;
        
        treeContent = assembler.buildTree(startStage, endStage, true);
    }
    
    public SelectItem [] getRangeOfSelectedStages() {
        int sIndex = 0;
        int eIndex = displayStageRange.length-1;
        
        for(int i=0;i<displayStageRange.length;i++){
            if(startStage.equalsIgnoreCase((String)displayStageRange[i].getValue())) {
                sIndex = i;
            }
            if(endStage.equalsIgnoreCase((String)displayStageRange[i].getValue())) {
                eIndex = i+1;
            }
        }
        
        SelectItem [] selectedRange = new SelectItem[eIndex-sIndex];
        for(int i=0;i<eIndex-sIndex;i++){
            selectedRange [i] = new SelectItem(displayStageRange[i+sIndex].getValue());
            
        }
        return selectedRange;
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
    
    public void setAnnotationTypes1(String [] value){
        annotationTypes1 = value;
    }
    
    public String [] getAnnotationTypes1() {
        return annotationTypes1;
    }
    
    public void setAnnotationTypes2(String [] value){
        annotationTypes2 = value;
    }
    
    public String [] getAnnotationTypes2() {
        return annotationTypes2;
    }
    
    public void setAnnotationTypes3(String [] value){
        annotationTypes3 = value;
    }
    
    public String [] getAnnotationTypes3() {
        return annotationTypes3;
    }
    
    public void setLocation1(String value){
        location1 = value;
    }
    
    public String getLocation1() {
        return location1;
    }
    
    public void setLocation2(String value){
        location2 = value;
    }
    
    public String getLocation2() {
        return location2;
    }
    
    public void setLocation3(String value){
        location3 = value;
    }
    
    public String getLocation3() {
        return location3;
    }
    
    public void setStartStage1(String value){    	
        startStage1 = value;
    }
    
    public String getStartStage1() {
        return startStage1;
    }
    
    public void setStartStage2(String value){
        startStage2 = value;
    }
    
    public String getStartStage2() {
        return startStage2;
    }
    
    public void setStartStage3(String value){
        startStage3 = value;
    }
    
    public String getStartStage3() {
        return startStage3;
    }
    
    public void setEndStage1(String value){
        endStage1 = value;
    }
    
    public String getEndStage1() {
        return endStage1;
    }
    
    public void setEndStage2(String value){
        endStage2 = value;
    }
    
    public String getEndStage2() {
        return endStage2;
    }
    
    public void setEndStage3(String value){
        endStage3 = value;
    }
    
    public String getEndStage3() {
        return endStage3;
    }
    
    public String getPattern1() {
        return pattern1;
    }
    
    public void setPattern1(String value) {
        pattern1 = value;
    }
    
    public String getPattern2() {
        return pattern2;
    }
    
    public void setPattern2(String value) {
        pattern2 = value;
    }
    
    public String getPattern3() {
        return pattern3;
    }
    
    public void setPattern3(String value) {
        pattern3 = value;
    }
    
    public String getLocations1() {
        return locations1;
    }
    
    public void setLocations1(String value) {
        locations1 = value;
    }
    
    public String getLocations2() {
        return locations2;
    }
    
    public void setLocations2(String value) {
        locations3 = value;
    }
    
    public String getLocations3() {
        return locations3;
    }
    
    public void setLocations3(String value) {
        locations3 = value;
    }
    
    public void setOperator1(String value){
        operator1 = value;
    }
    
    public String getOperator1() {
        return operator1;
    }
    
    public void setOperator2(String value){
        operator2 = value;
    }
    
    public String getOperator2() {
        return operator2;
    }

    public UIInput getStartInput() { return startInput; }
    public void setStartInput(UIInput value) { startInput = value; }
    
    public UIInput getEndInput() { return endInput; }
    public void setEndInput(UIInput value) { endInput = value; }
    
    
    
    public SelectItem [] getAnnotationVals() {
        String possOrUncertainParam = "";
        
        if(Utility.getProject().equals("GUDMAP")){
            possOrUncertainParam = "uncertain";
        }
        
        return new SelectItem []
        { new SelectItem("p","present"), new SelectItem("nd","not detected"), new SelectItem("u",possOrUncertainParam) };
    }
    
    public SelectItem [] getLocationVals() {
        return new SelectItem []
        { new SelectItem("in","in"), new SelectItem("adjacent to", "adjacent to") };
    }
    
    public SelectItem [] getPatternVals() {
        return new SelectItem []
        { new SelectItem("","-- pattern --"),
        new SelectItem("graded", "graded"),
        new SelectItem("regional","regional"), 
        new SelectItem("restricted", "restricted"),
        new SelectItem("single cell", "single cell"),
        new SelectItem("spotted", "spotted"), 
        new SelectItem("ubiquitous", "ubiquitous")};
    }
    
    public SelectItem [] getLocationsVals() {
        return new SelectItem []
        { new SelectItem("","-- location --"),
          new SelectItem("caudal","caudal"),
          new SelectItem("deep","deep"),
          new SelectItem("distal","distal"),
          new SelectItem("dorsal","dorsal"),
          new SelectItem("lateral", "lateral"),
          new SelectItem("medial","medial"),
          new SelectItem("proximal","proximal"),
          new SelectItem("radial","radial"),
          new SelectItem("rostral","rostral"),
          new SelectItem("surface","surface"),
          new SelectItem("ventral", "ventral") };
    }
    
    public SelectItem [] getResultFormat() {
        return new SelectItem []
        { new SelectItem(Utility.getProject(),Utility.getProject()+" entries"),
          new SelectItem("GENE","genes") /*,
          new SelectItem("TF","transcription factors")*/
          };
    }
    
    public SelectItem[] getAvailableStages() {
        return displayStageRange;
    }
    
    public ArrayList getTreeContent(){
        return treeContent;
    }
    
	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}
	
    public void displayTree(ActionEvent e) {
        treeContent = assembler.buildTree(startStage, endStage, true);
    }
    
    public void validateStages(FacesContext context, UIComponent component, Object value) {
        String s = (String) startInput.getLocalValue();
        String e = (String) endInput.getLocalValue();
        
        boolean stageRangeValid = assembler.stageRangesAreValid(s,e);
        
        if(!stageRangeValid) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a valid stage range. ", "Please select a valid stage range. ");
            throw new ValidatorException(message);
        }
    }

    /*************************************************************************
     * Action Methods
     * 
     */
	public String goSearch() {
		String message = validateQueryString1();
//		System.out.println("validation message:"+message);
		if (message != null)
			return MessageBean.showMessage("Your query string is not valid, " + message + ". Please try again.");
		
		BooleanQueryParserDelegate parser = new BooleanQueryParserDelegate();	// this is a proper parser 
		message = parser.parse(input);	// this is a proper parser 
		if (message != null)
			return MessageBean.showMessage("Your query string is not valid, " + message + " Please try again.", true);
		input = parser.getCode();
		
    	FacesUtil.setSessionValue("input", input);
    	return "queryresult";
    }
    
    public void saveQuery(ActionEvent ae){
        //We must get first our context
        FacesContext context = FacesContext.getCurrentInstance();
        
        if(input != null && !input.equals("")) {
            //Then we have to get the Response where to write our file
            HttpServletResponse response = 
                     ( HttpServletResponse ) context.getExternalContext().getResponse();
                     
            String contentType = "application/text;charset=iso-8859-1";
            String header = "attachment;filename=\"booleanQuery.txt\"";
            FileHandler.saveStringToDesktop(response, input, header, contentType);
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public String clearQuery(){
    	input = "";
        return "";

    }
    
    private String validateQueryString1() {	// Needs a proper parser to do a comprehensive validation
//    	System.out.println("BooleanTestBean:validateQueryString:input:"+input);
		if(input == null || input.equals("")) 
			return "query string is empty";
		
		if(input.indexOf(":") < 0) 
			return "invalid prefix";
		
		int colonPos = input.indexOf(":");
//		System.out.println("BooleanTestBean:validateQueryString:colonPos" + colonPos);
		String prefix = input.substring(0, colonPos).trim();
		int queryStringLength = input.trim().length();
//		System.out.println("BooleanTestBean:validateQueryString:queryStringLength" + queryStringLength);
		if (prefix == null) 
			return "query string prefix (gene/TF) is missing";
		
		if (colonPos == queryStringLength-1) { // user only input prefix
			return "query string is not completed";
		}

		if (prefix.equalsIgnoreCase("gudmap"))
			return null;
		
		if (prefix.equalsIgnoreCase("euregene"))
			return null;
		
		if (prefix.equalsIgnoreCase("gene") || prefix.equalsIgnoreCase("genes"))
			return null;
		
		if (prefix.equalsIgnoreCase("TF") || prefix.equalsIgnoreCase("transcription factor"))
			return null;
		
//		return "query string prefix should be either GUDMAP: or GENE:";
		return "query string prefix should be either GUDMAP:, ERG:, or GENE:";
    }

    
}