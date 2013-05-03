package gmerg.entities.submission;

import java.util.ArrayList;

public class Submission {

    private String accID;
    private Specimen specimen;
    private Person submitter;
    private String stage;
    private String authors;
    private ArrayList originalImages;
    private int publicFlag;
    private String assayType;
    private String archiveId;
    
    private int deleteFlag; // SUB_IS_DELETED
    private int submitterId; //  SUB_SUBMITTER_FK: 3
    private int piId; // SUB_PI_FK: 3
    private int entryBy; // SUB_ENTRY_BY_FK: 1
    private int modifierId; // SUB_MODIFIER_FK: 1
    private int localStatusId; // SUB_LOCAL_STATUS_FK: 3
    private int dbStatusId; // SUB_DB_STATUS_FK: 2
    private String project; // SUB_PROJECT_FK: GUDMAP
    private int batchId; // SUB_BATCH: 111
    private String nameSpace; // SUB_NAMESPACE: http://www.gudmap.org
    private String osAccession; // SUB_OS_ACCESSION: UNASSIGNED
    private String loaclId; // SUB_LOCAL_ID:
    private String source; // SUB_SOURCE:
    private String validation; // SUB_VALIDATION:
    private int control; // SUB_CONTROL: 0
    private String assessment; // SUB_ASSESSMENT:
    private int confidencLevel; // SUB_CONFIDENCE: 0
    private String localDbName; // SUB_LOCALDB_NAME:
    private String labId; // SUB_LAB_ID: 
    private String localId; //SUB_LOCAL_ID
    private String euregeneId; //SUB_ACCESSION_ID_2

    protected String[] resultNotes = null;
    
    private Person[] principalInvestigators;

    public Submission() {
    
    }
    
    public Submission(String accID, Specimen specimen, Person[] pis, Person submitter, 
    		String stage, String authors, ArrayList originalImages) {
    	this.accID = accID;
    	this.specimen = specimen;
    	this.principalInvestigators = pis;
    	this.submitter = submitter;
    	this.stage = stage;
    	this.authors = authors;
        this.originalImages = originalImages;
    }
    
    public void setAccID(String id) {
        accID = id;
    }

    public String getAccID() {
        return accID;
    }

    public void setSpecimen(Specimen spn) {
        specimen = spn;
    }

    public Specimen getSpecimen() {
        return specimen;
    }

    public void setSubmitter(Person sbmttr) {
        submitter = sbmttr;
    }

    public Person getSubmitter() {
        return submitter;
    }

    public void setStage(String stg) {
        stage = stg;
    }

    public String getStage() {
        return stage;
    }

    public void setAuthors(String aut) {
        authors = aut;
    }

    public String getAuthors() {
        return authors;
    }
    
    public ArrayList getOriginalImages() {
        return originalImages;
    }

    public void setOriginalImages(ArrayList images) {
        originalImages = images;
    }
    
    public int getPublicFlag() {
    	return publicFlag;
    }
    
    public void setPublicFlag(int publicFlag) {
    	this.publicFlag = publicFlag;
    }
    
    public String getAssayType() {
    	return assayType;
    }
    
    public void setAssayType(String assayType) {
    	this.assayType = assayType;
    }
    
    public String getArchiveId() {
        return archiveId;
    }
    
    public void setArchiveId(String aId){
        archiveId = aId;
    }
    
    public int getDeleteFlag() {
    	return deleteFlag;
    }
    
    public void setDeleteFlag(int deleteFlag) {
    	this.deleteFlag = deleteFlag;
    }
    
    public int getSubmitterId() {
    	return submitterId;
    }
    
    public void setSubmitterId(int submitterId) {
    	this.submitterId = submitterId;
    }
    
    public int getPiId() {
    	return piId;
    }
    
    public void setPiId(int piId) {
    	this.piId = piId;
    }

    public int getEntryBy() {
    	return entryBy;
    }
    
    public void setEntryBy(int entryBy) {
    	this.entryBy = entryBy;
    }

    public int getModifierId() {
    	return modifierId;
    }
    
    public void setModifierId(int modifierId) {
    	this.modifierId = modifierId;
    }

    public int getLocalStatusId() {
    	return localStatusId;
    }
    
    public void setLocalStatusId(int localStatusId) {
    	this.localStatusId = localStatusId;
    }

    public int getDbStatusId() {
    	return dbStatusId;
    }
    
    public void setDbStatusId(int dbStatusId) {
    	this.dbStatusId = dbStatusId;
    }

    public String getProject() {
    	return project;
    }
    
    public void setProject(String project) {
    	this.project = project;
    }
    public int getBatchId() {
    	return batchId;
    }
    
    public void setBatchId(int batchId) {
    	this.batchId =batchId;
    }

    public String getNameSpace() {
    	return nameSpace;
    }
    
    public void setNameSpace(String nameSpace) {
    	this.nameSpace = nameSpace;
    }

    public String getOsAccession() {
    	return osAccession;
    }
    
    public void setOsAccession(String osAccession) {
    	this.osAccession = osAccession;
    }

    public String getLoaclId() {
    	return loaclId;
    }
    
    public void setLoaclId(String loaclId) {
    	this.loaclId = loaclId;
    }

    public String getSource() {
    	return source;
    }
    
    public void setSource(String source) {
    	this.source = source;
    }

    public String getValidation() {
    	return validation;
    }
    
    public void setValidation(String validation) {
    	this.validation = validation;
    }

    public int getControl() {
    	return control;
    }
    
    public void setControl(int control) {
    	this.control = control;
    }

    public String getAssessment() {
    	return assessment;
    }
    
    public void setAssessment(String assessment) {
    	this.assessment = assessment;
    }

    public int getConfidencLevel() {
    	return confidencLevel;
    }
    
    public void setConfidencLevel(int confidencLevel) {
    	this.confidencLevel = confidencLevel;
    }

    public String getLocalDbName() {
    	return localDbName;
    }
    
    public void setLocalDbName(String localDbName) {
    	this.localDbName = localDbName;
    }

    public String getLabId() {
    	return labId;
    }
    
    public void setLabId(String labId) {
    	this.labId = labId;
    }

    public void setPrincipalInvestigators(Person[] pis) {
        this.principalInvestigators = pis;
    }
    
    public Person[] getPrincipalInvestigators() {
        return principalInvestigators;
    }

    public String getEuregeneId() {
    	return euregeneId;
    }
    
    public void setEuregeneId(String euregeneId) {
    	this.euregeneId = euregeneId;
    }

    public String[] getResultNotes() {
    	return resultNotes;
    }
    
    public void setResultNotes(String[] input) {
    	resultNotes = input;
	if (null != resultNotes && 0 == resultNotes.length)
	    resultNotes = null;
    }    
}
