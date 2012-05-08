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
    private Transgenic[] transgenics;// added by xingjun - 09/11/2009
    private boolean multipleTransgenics; // added by xingjun - 09/11/2009
    private String tissue; // added by bernie - 15/09/2010

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

    // added by xingjun - 22/07/2009
    public Transgenic getTransgenic() {
    	return this.transgenic;
    }
    
    public void setTransgenic(Transgenic transgenic) {
    	this.transgenic = transgenic;
    }

    // xingjun - 09/11/2009
    public Transgenic[] getTransgenics() {
    	return this.transgenics;
    }
    
    public void setTransgenics(Transgenic[] transgenics) {
    	this.transgenics = transgenics;
    }
    
    public boolean isMultipleTransgenics() {
    	return this.multipleTransgenics;
    }
    
    public void setMultipleTransgenics(boolean multipleTransgenics) {
    	this.multipleTransgenics = multipleTransgenics;
    }    
    
    // added by Bernie - 23/09/2020
    public String getTissue() {
    	return tissue;
    }

    public void setTissue(String tissue) {
    	this.tissue = tissue;
    }
    
    
}
