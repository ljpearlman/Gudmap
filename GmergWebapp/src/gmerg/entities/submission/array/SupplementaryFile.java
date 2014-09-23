/**
 * 
 */
package gmerg.entities.submission.array;

import java.util.List;

import gmerg.entities.submission.nextgen.NGDSupFiles;

/**
 * @author xingjun
 *
 */
public class SupplementaryFile {
	
    private String filesLocation;
    private String celFile;
    private String chpFile;
    private String rptFile;
    private String expFile;
    private String txtFile;
    
    private List<NGDSupFiles> rawFiles;
    private List<NGDSupFiles> processedFiles;
    
    public SupplementaryFile() {
    	
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
    
    public void setRawFile(List<NGDSupFiles> file) {
        rawFiles = file;
    }

    public List<NGDSupFiles> getRawFile() {
        return rawFiles;
    }

    public void setProcessedFile(List<NGDSupFiles> file) {
       processedFiles = file;
    }

    public List<NGDSupFiles> getProcessedFile() {
        return processedFiles;
    }
    

}
