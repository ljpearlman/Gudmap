package gmerg.entities.submission.nextgen;

public class Protocol {

	private String name;
	private String libraryConProt;
    private String libraryStrategy;
    private String extractedMolecule;
    private String rnaIsolationMethod;
    private String sequencingMethod;
    private String labelMethod;
    private String instrumentModel;
    private String pairedEnd;
    private String protAdditionalNotes;
    private String dnaExtractMethod;
    private String antibody;
    private String createdBy;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public void setLibraryConProt(String libraryConProt) {
    	this.libraryConProt = libraryConProt;
    }

    public String getLibraryConProt() {
        return libraryConProt;
    }

    public void setLibraryStrategy(String libraryStrategy) {
    	this.libraryStrategy = libraryStrategy;
    }

    public String getLibraryStrategy() {
        return libraryStrategy;
    }

    

    public void setExtractedMolecule(String extractedMolecule) {
    	this.extractedMolecule = extractedMolecule;
    }

    public String getExtractedMolecule() {
        return extractedMolecule;
    }

    public void setRnaIsolationMethod(String rnaIsolationMethod) {
    	this.rnaIsolationMethod = rnaIsolationMethod;
    }

    public String getRnaIsolationMethod() {
        return rnaIsolationMethod;
    }

    public void setSequencingMethod(String sequencingMethod) {
    	this.sequencingMethod = sequencingMethod;
    }

    public String getSequencingMethod() {
        return sequencingMethod;
    }

    public void setLabelMethod(String labelMethod) {
    	this.labelMethod = labelMethod;
    }

    public String getLabelMethod() {
        return labelMethod;
    }

    public void setInstrumentModel(String instrumentModel) {
    	this.instrumentModel = instrumentModel;
    }

    public String getInstrumentModel() {
        return instrumentModel;
    }

    public void setPairedEnd(String pairedEnd) {
    	this.pairedEnd = pairedEnd;
    }

    public String getPairedEnd() {
        return pairedEnd;
    }
    
    public void setProtAdditionalNotes(String protAdditionalNotes) {
    	this.protAdditionalNotes = protAdditionalNotes;
    }

    public String getProtAdditionalNotes() {
        return protAdditionalNotes;
    }
    
    public void setDnaExtractMethod(String dnaExtractMethod) {
    	this.dnaExtractMethod = dnaExtractMethod;
    }

    public String getDnaExtractMethod() {
        return dnaExtractMethod;
    }
    
    public void setAntibody(String antibody) {
    	this.antibody = antibody;
    }

    public String getAntibody() {
        return antibody;
    }
    
    public void setCreatedBy(String createdBy) {
    	this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

}
