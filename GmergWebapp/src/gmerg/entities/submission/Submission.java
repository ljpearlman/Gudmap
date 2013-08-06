package gmerg.entities.submission;

import java.util.ArrayList;

public class Submission {

    protected String accID;
    protected Specimen specimen;
    protected Person submitter;
    protected String stage;
    protected String authors;
    protected ArrayList originalImages;
    protected int publicFlag;
    protected String assayType;
    protected String archiveId;

    protected int deletedFlag; // SUB_IS_DELETED
    protected int submitterId; //  SUB_SUBMITTER_FK: 3
    protected int piId; // SUB_PI_FK: 3
    protected int entryBy; // SUB_ENTRY_BY_FK: 1
    protected int modifierId; // SUB_MODIFIER_FK: 1
    protected int localStatusId; // SUB_LOCAL_STATUS_FK: 3
    protected int dbStatusId; // SUB_DB_STATUS_FK: 2
    protected String project; // SUB_PROJECT_FK: GUDMAP
    protected int batchId; // SUB_BATCH: 111
    protected String nameSpace; // SUB_NAMESPACE: http://www.gudmap.org
    protected String osAccession; // SUB_OS_ACCESSION: UNASSIGNED
    protected String loaclId; // SUB_LOCAL_ID:
    protected String source; // SUB_SOURCE:
    protected String validation; // SUB_VALIDATION:
    protected int control; // SUB_CONTROL: 0
    protected String assessment; // SUB_ASSESSMENT:
    protected int confidencLevel; // SUB_CONFIDENCE: 0
    protected String localDbName; // SUB_LOCALDB_NAME:
    protected String labId; // SUB_LAB_ID: 
    protected String localId; //SUB_LOCAL_ID
    protected String euregeneId; //SUB_ACCESSION_ID_2

    protected String[] resultNotes = null;
    
    protected Person[] principalInvestigators;
    protected Allele[] allele;

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
    

    public Allele[] getAllele() {
        return allele;
    }

    public void setAllele(Allele[] input) {
        allele = input;
        int iSize = 0;
	if (null != allele)
	    iSize = allele.length;

	if (0 == iSize) {
	    allele = null;
	    return;
	}

	if (1 == iSize)
	    allele[0].setTitle("Non-wild type Allele:");
	else {
	    int i = 0;
	    iSize++;
	    for (i = 1; i < iSize; i++) {
		allele[i-1].setTitle("Non-wild type Allele "+i+":");
	    }
	}
    }

    public String getGeneSymbol() {
	String ret = null;
	if (null != allele && 0 < allele.length)
	    ret = allele[0].getGeneSymbol();
    
	if (null != ret && ret.trim().equals(""))
	    ret = null;
	
	return ret;
    }
    public String getGeneName() {
	return null;
    }

    public boolean isTransgenic() {
	if (null != assayType && assayType.equalsIgnoreCase("tg"))
	    return true;

	return false;
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
    
    public int getNumImages() {
	int ret = 0;
	if (null != originalImages)
	    ret = originalImages.size();
	
	return ret;
    }

    public ArrayList getOriginalImages() {
	// 4 image a row
	int iSize = 0;
	if (null != originalImages)
	    iSize = originalImages.size();
	if (0 == iSize)
	    return null;

	ArrayList ret = new ArrayList();
	ImageInfo[] row = null;
	int index = 0;
	int  i = 0;
	while (index < iSize) {
	    row = new ImageInfo[4];
	    for (i = 0; i < 4; i++)
		row[i] = null;
	    for (i = 0; i < 4; i++) 
		if (index < iSize) {
		    row[i] = (ImageInfo)originalImages.get(index);
		    index++;
		}
	    ret.add(row);
	}

	return ret;
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
    
    public void setAssayType(String input) {
    	assayType = input;
	if (null != assayType) 
	    assayType = assayType.trim();
    }
    
    public String getArchiveId() {
        return archiveId;
    }
    
    public void setArchiveId(String aId){
        archiveId = aId;
    }
    
    public int getDeletedFlag() {
    	return deletedFlag;
    }
    
    public void setDeletedFlag(int input) {
    	this.deletedFlag = input;
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

    public boolean isReleased() {
	if (publicFlag < 1)
	    return false;
	return true;
    }
    
    public boolean isDeleted() {
	if (deletedFlag < 1)
	    return false;
	return true;
    }
}
