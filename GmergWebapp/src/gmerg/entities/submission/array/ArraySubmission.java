package gmerg.entities.submission.array;

import gmerg.entities.submission.Submission;
import gmerg.entities.submission.Transgenic;

public class ArraySubmission extends Submission {

    private Series series;
    private Platform platform;
    private Sample sample;
    private String filesLocation;
    private String celFile;
    private String chpFile;
    private String rptFile;
    private String expFile;
    private String txtFile;
    private Transgenic transgenic;
    private Transgenic[] transgenics;
    private boolean multipleTransgenics; 
    private String tissue; 
    private boolean isPublic;
    private boolean isDeleted;

    public void setSeries(Series ser) {
        series = ser;
    }

    public Series getSeries() {
        return series;
    }

    public void setPlatform(Platform pform) {
        platform = pform;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setSample(Sample smpl) {
        sample = smpl;
    }

    public Sample getSample() {
        return sample;
    }

    public void setFilesLocation(String location) {
        filesLocation = location;
    }

    public String getFilesLocation() {
        return filesLocation;
    }

    public void setCelFile(String file) {
        celFile = file;
    }

    public String getCelFile() {
        return celFile;
    }

    public void setChpFile(String file) {
        chpFile = file;
    }

    public String getChpFile() {
        return chpFile;
    }

    public void setRptFile(String file) {
        rptFile = file;
    }

    public String getRptFile() {
        return rptFile;
    }

    public void setExpFile(String file) {
        expFile = file;
    }

    public String getExpFile() {
        return expFile;
    }

    public void setTxtFile(String file) {
        txtFile = file;
    }

    public String getTxtFile() {
        return txtFile;
    }

    public Transgenic getTransgenic() {
    	return transgenic;
    }
    
    public void setTransgenic(Transgenic transgenic) {
    	this.transgenic = transgenic;
    }

    public Transgenic[] getTransgenics() {
    	return transgenics;
    }
    
    public void setTransgenics(Transgenic[] transgenics) {
    	this.transgenics = transgenics;
    }
    
    public boolean isMultipleTransgenics() {
    	return multipleTransgenics;
    }
    
    public void setMultipleTransgenics(boolean multipleTransgenics) {
    	this.multipleTransgenics = multipleTransgenics;
    }    
    
    public String getTissue() {
    	return tissue;
    }

    public void setTissue(String tissue) {
    	this.tissue = tissue;
    }
    
    public Boolean getPublic() {
        return this.isPublic;
    }
    
    public void setPublic(int  publicFlag) {
    	if (publicFlag == 1)
    		this.isPublic = true;
    	else
    		this.isPublic = false;
    }
    
    public Boolean getDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(int deleteFlag) {
    	if (deleteFlag == 1)
    		this.isDeleted = true;
    	else
    		this.isDeleted = false;
    }
    
}
