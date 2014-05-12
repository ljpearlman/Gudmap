package gmerg.entities.submission.nextgen;

import gmerg.entities.submission.array.Sample;

public class NGDSample extends Sample {

   
	private String stageFormat;
    private String genotype;
    //private String strain;
    private String sampleNotes;
    private String libraryPoolSize;
    private String libraryReads;
    private String readLength;
    private String meanInsertSize;
    

    
    public void setStageFormat(String stageFormat) {
    	this.stageFormat = stageFormat;
    }

    public String getStageFormat() {
        return stageFormat;
    }

    public void setGenotype(String genotype) {
    	this.genotype = genotype;
    }

    public String getGenotype() {
        return genotype;
    }
    
   /* public void setStrain(String strain) {
        this.strain = strain;
    }

    public String getStrain() {
        return strain;
    }*/
    public void setSampleNotes(String sampleNotes) {
        this.sampleNotes = sampleNotes;
    }

    public String getSampleNotes() {
        return sampleNotes;
    }
    
    public void setLibraryPoolSize(String libraryPoolSize) {
        this.libraryPoolSize = libraryPoolSize;
    }

    public String getLibraryPoolSize() {
        return libraryPoolSize;
    }
    
    public void setLibraryReads(String libraryReads) {
        this.libraryReads = libraryReads;
    }

    public String getLibraryReads() {
        return libraryReads;
    }
    
    public void setReadLength(String readLength) {
        this.readLength = readLength;
    }

    public String getReadLength() {
        return readLength;
    }
    
    public void setMeanInsertSize(String meanInsertSize) {
        this.meanInsertSize = meanInsertSize;
    }

    public String getMeanInsertSize() {
        return meanInsertSize;
    }

}
