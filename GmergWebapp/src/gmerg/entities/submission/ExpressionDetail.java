package gmerg.entities.submission;

import java.util.ArrayList;

public class ExpressionDetail {
    private String submissionId;
    private String stage;
    private String componentName;
    private String componentId;
    private ArrayList componentDescription;
    private String primaryStrength;
    private String secondaryStrength;
    private String expressionImage;
    private ExpressionPattern[] pattern;
    private String expressionNote;
    private int expressionId;
    private int submissionFk;
    private boolean annotated;
    private boolean noteExists;
    private int submissionDbStatus;
    
    private String densityImageRelativeToTotal;
    private String densityImageRelativeToAge;
    private String densityRelativeToTotal;
    private String densityDirectionalChange;
    private String densityMagnitudeChange;
    private String densityChange;
    private String densityNote;
    private String densityComponentId;
    
    public String getSubmissionId(){
        return submissionId;
    }
    
    public void setSubmissionId(String value) {
        submissionId = value;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public ArrayList getComponentDescription() {
        return componentDescription;
    }

    public void setComponentDescription(ArrayList componentDescription) {
        this.componentDescription = componentDescription;
    }

    public String getPrimaryStrength() {
        return primaryStrength;
    }

    public void setPrimaryStrength(String strength) {
        this.primaryStrength = strength;
    }

    public String getSecondaryStrength() {
        return secondaryStrength;
    }

    public void setSecondaryStrength(String strength) {
        this.secondaryStrength = strength;
    }
    
    public void setExpressionImage(String value){
        expressionImage = value;
    }
    
    public String getExpressionImage() {
        return expressionImage;
    }

    public ExpressionPattern[] getPattern() {
        return pattern;
    }

    public void setPattern(ExpressionPattern[] pattern) {
        this.pattern = pattern;
    }

    public String getExpressionNote() {
        return expressionNote;
    }

    public void setExpressionNote(String expressionNote) {
        this.expressionNote = expressionNote;
    }

    public int getExpressionId() {
        return expressionId;
    }

    public void setExpressionId(int expressionId) {
        this.expressionId = expressionId;
    }

    public int getSubmissionFk() {
        return submissionFk;
    }

    public void setSubmissionFk(int submissionFk) {
        this.submissionFk = submissionFk;
    }
    
    public boolean isAnnotated() {
    	return annotated;
    }
    
    public void setAnnotated(boolean annotated) {
    	this.annotated = annotated;
    }
    
    public boolean isNoteExists(){
        return noteExists;
    }
    
    public void setNoteExists(boolean value){
        noteExists = value;
    }
    
    public int getSubmissionDbStatus() {
    	return submissionDbStatus;
    }
    
    public void setSubmissionDbStatus(int submissionDbStatus) {
    	this.submissionDbStatus = submissionDbStatus;
    }
    
    public void setDensityImageRelativeToTotal(String value){
    	densityImageRelativeToTotal = value;
    }
    
    public String getDensityImageRelativeToTotal() {
        return densityImageRelativeToTotal;
    }

    public void setDensityImageRelativeToAge(String value){
    	densityImageRelativeToAge = value;
    }
    
    public String getDensityImageRelativeToAge() {
        return densityImageRelativeToAge;
    }

    public String getDensityRelativeToTotal() {
        return densityRelativeToTotal;
    }

    public void setDensityRelativeToTotal(String densityRelativeToTotal) {
        this.densityRelativeToTotal = densityRelativeToTotal;
    }

    public String getDensityDirectionalChange() {
        return densityDirectionalChange;
    }

    public void setDensityDirectionalChange(String densityDirectionalChange) {
        this.densityDirectionalChange = densityDirectionalChange;
    }

    public String getDensityMagnitudeChange() {
        return densityMagnitudeChange;
    }

    public void setDensityMagnitudeChange(String densityMagnitudeChange) {
        this.densityMagnitudeChange = densityMagnitudeChange;
    }

    public String getDensityNote() {
        return densityNote;
    }

    public void setDensityNote(String densityNote) {
        this.densityNote = densityNote;
    }

    public String getDensityComponentId() {
        return densityComponentId;
    }

    public void setDensityComponentId(String value) {
        this.densityComponentId = value;
    }

    public String getDensityChange() {
    	if (densityDirectionalChange == null && densityMagnitudeChange == null)
    		densityChange = "";
    	else if (densityDirectionalChange == null && densityMagnitudeChange != null)
    		densityChange = densityMagnitudeChange;
    	else if (densityDirectionalChange != null && densityMagnitudeChange == null)
    		densityChange = densityDirectionalChange.replace("ed", "e");
    	else
    		densityChange = densityDirectionalChange.replace("ed", "e, ") + densityMagnitudeChange.toLowerCase();
    	
        return densityChange;
    }
    
}
