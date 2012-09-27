package gmerg.entities.submission;

import java.util.ArrayList;

public class Submission {

    private String accID;
    private Specimen specimen;
    private Person principalInvestigator;
    private Person submitter;
    private String stage;
    private String authors;
    private ArrayList originalImages;
    private int publicFlag;
    private String assayType;
    private String archiveId;
    
    // xingjun - 15/07/2008 - need more information - for duplicating submission purpose
    private int deleteFlag; // SUB_IS_DELETED
    private int submitterId; //  SUB_SUBMITTER_FK: 3
    private int piId; // SUB_PI_FK: 3
    private int entryBy; // SUB_ENTRY_BY_FK: 1
    private int modifierId; // SUB_MODIFIER_FK: 1
    private int localStatusId; // SUB_LOCAL_STATUS_FK: 3
    private int dbStatusId; // SUB_DB_STATUS_FK: 2
    private String project; // SUB_PROJECT_FK: GUDMAP
    private int authorId; // SUB_AUTHOR_REF: 0
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
    
    // xingjun  - 14/06/2011 - might have more than one principal investigators
    private Person[] principalInvestigators;

    public Submission() {
    
    }
    
    public Submission(String accID, Specimen specimen, Person pi, Person submitter, 
    		String stage, String authors, ArrayList originalImages) {
    	this.accID = accID;
    	this.specimen = specimen;
    	this.principalInvestigator = pi;
    	this.submitter = submitter;
    	this.stage = stage;
    	this.authors = authors;
        this.originalImages = originalImages;
    }
    
    // xingjun - 14/06/2011 - multiple pis constructor
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

    public void setPrincipalInvestigator(Person pI) {
        principalInvestigator = pI;
    }

    public Person getPrincipalInvestigator() {
        return principalInvestigator;
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
    	return this.publicFlag;
    }
    
    public void setPublicFlag(int publicFlag) {
    	this.publicFlag = publicFlag;
    }
    
    public String getAssayType() {
    	return this.assayType;
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
    
    // xingjun - 15/07/2008
    public int getDeleteFlag() {
    	return this.deleteFlag;
    }
    
    public void setDeleteFlag(int deleteFlag) {
    	this.deleteFlag = deleteFlag;
    }
    
    public int getSubmitterId() {
    	return this.submitterId;
    }
    
    public void setSubmitterId(int submitterId) {
    	this.submitterId = submitterId;
    }
    
    public int getPiId() {
    	return this.piId;
    }
    
    public void setPiId(int piId) {
    	this.piId = piId;
    }

    public int getEntryBy() {
    	return this.entryBy;
    }
    
    public void setEntryBy(int entryBy) {
    	this.entryBy = entryBy;
    }

    public int getModifierId() {
    	return this.modifierId;
    }
    
    public void setModifierId(int modifierId) {
    	this.modifierId = modifierId;
    }

    public int getLocalStatusId() {
    	return this.localStatusId;
    }
    
    public void setLocalStatusId(int localStatusId) {
    	this.localStatusId = localStatusId;
    }

    public int getDbStatusId() {
    	return this.dbStatusId;
    }
    
    public void setDbStatusId(int dbStatusId) {
    	this.dbStatusId = dbStatusId;
    }

    public String getProject() {
    	return this.project;
    }
    
    public void setProject(String project) {
    	this.project = project;
    }

    public int getAuthorId() {
    	return this.authorId;
    }
    
    public void setAuthorId(int authorId) {
    	this.authorId = authorId;
    }

    public int getBatchId() {
    	return this.batchId;
    }
    
    public void setBatchId(int batchId) {
    	this.batchId =batchId;
    }

    public String getNameSpace() {
    	return this.nameSpace;
    }
    
    public void setNameSpace(String nameSpace) {
    	this.nameSpace = nameSpace;
    }

    public String getOsAccession() {
    	return this.osAccession;
    }
    
    public void setOsAccession(String osAccession) {
    	this.osAccession = osAccession;
    }

    public String getLoaclId() {
    	return this.loaclId;
    }
    
    public void setLoaclId(String loaclId) {
    	this.loaclId = loaclId;
    }

    public String getSource() {
    	return this.source;
    }
    
    public void setSource(String source) {
    	this.source = source;
    }

    public String getValidation() {
    	return this.validation;
    }
    
    public void setValidation(String validation) {
    	this.validation = validation;
    }

    public int getControl() {
    	return this.control;
    }
    
    public void setControl(int control) {
    	this.control = control;
    }

    public String getAssessment() {
    	return this.assessment;
    }
    
    public void setAssessment(String assessment) {
    	this.assessment = assessment;
    }

    public int getConfidencLevel() {
    	return this.confidencLevel;
    }
    
    public void setConfidencLevel(int confidencLevel) {
    	this.confidencLevel = confidencLevel;
    }

    public String getLocalDbName() {
    	return this.localDbName;
    }
    
    public void setLocalDbName(String localDbName) {
    	this.localDbName = localDbName;
    }

    public String getLabId() {
    	return this.labId;
    }
    
    public void setLabId(String labId) {
    	this.labId = labId;
    }
    // xingjun - 15/07/2008

    // xingjun - 14/06/2011 
    public void setPrincipalInvestigators(Person[] pis) {
        this.principalInvestigators = pis;
    }
    
    public Person[] getPrincipalInvestigators() {
        return this.principalInvestigators;
    }

    public String getLocalId() {
    	return this.localId;
    }
    
    public void setLocalId(String localId) {
    	this.localId = localId;
    }
 
}
